package com.piesat.schedule.sync.client.util;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.DateUtils;
import com.piesat.schedule.sync.client.constants.ComConstants;
import com.piesat.schedule.sync.client.constants.SqlConstants;
import com.piesat.schedule.sync.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.sync.client.datasource.DynamicDataSource;
import com.piesat.schedule.sync.client.vo.SyncComVo;
import com.piesat.util.ResultT;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * 数据库导出文件
 *
 * @author cwh
 * @date 2020年 11月18日 15:16:11
 */
public class DataExport {
    public static Semaphore semaphore = new Semaphore(50);
    private final long batchCount = 100000;

    public void export(String databasePid, String tableName, List<String> columns, List<String> where, String saveDir, SyncComVo syncComVo, ResultT<String> resultT) throws SQLException, IOException, InterruptedException {
        Connection con = null;
        try {
            semaphore.acquire();
            String dir = DateUtils.dateTime();
            saveDir += File.separator + dir + File.separator + tableName + ComConstants.TEXT_SUFFIX;

            DataSourceContextHolder.setDataSource(databasePid);
            String whereStr = where.stream().collect(Collectors.joining(ComConstants.AND));
            String column = columns.stream().collect(Collectors.joining(ComConstants.SEPARATOR));
            String sql = String.format(SqlConstants.QUERY_SQL, column, tableName, whereStr);
            DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
            con = dynamicDataSource.getConnection();
            Statement s = con.createStatement();
            s.setFetchSize(1000);
            ResultSet r = s.executeQuery(sql);
            ResultSetMetaData metaData = r.getMetaData();

            //数据库每行数据
            StringBuilder rowData = new StringBuilder();

            //单次写入文件的数据(可以删掉)
            List<String> pageData = new LinkedList<String>();

            //result 为数据库
            int j = 0;
            int num = metaData.getColumnCount();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String name = metaData.getColumnName(i).toUpperCase();
                rowData.append(name).append("\t");
            }
            while (r.next()) {
                j++;
                //组装单行数据
                for (int i = 1; i <= num; i++) {
                    Object obj = r.getObject(i);
                    if (obj instanceof Date) {
                        rowData.append(DateUtils.getDateTimeStr((Date) obj));
                    } else if (obj instanceof Blob) {
                        rowData.append(this.getString(null, (Blob) obj));
                    } else if (obj instanceof Clob) {
                        rowData.append(this.getString((Clob) obj, null));
                    } else {
                        rowData.append(obj);
                    }
                    rowData.append("\t");
                }
                pageData.add(rowData.toString());
                rowData.setLength(0);

                //10W条数据写一次，视情况而定
                if (j % batchCount == 0) {
                    this.writeFile(pageData, saveDir);
                    pageData.clear(); //清空list
                }
            }

            if (j % batchCount != 0) {
                this.writeFile(pageData, saveDir);
            }
            resultT.setSuccessMessage("导出物理库：{}，表名：{}，导出时间段：{}，导出条数：{}", databasePid, tableName, whereStr, j);
        } catch (Exception e) {
            throw e;
        } finally {
            semaphore.release();
            DataSourceContextHolder.clearDataSource();
            if (con != null) {
                con.close();
            }
            syncComVo.setConditions(saveDir);
        }
    }


    /**
     * 写数据到文件
     */
    public void writeFile(List<String> data, String path) throws IOException {

        if (null == data || data.size() == 0) {
            return;
        }

        File file = new File(path);

        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(path, true);
        OutputStreamWriter write = new OutputStreamWriter(fileOutputStream, "UTF-8");
        BufferedWriter writer = new BufferedWriter(write);
        for (String str : data) {
            writer.write(str);
            writer.flush();
        }
        writer.close();
        write.close();
        fileOutputStream.close();
    }

    public String getString(Clob c, Blob b) throws SQLException, UnsupportedEncodingException {
        String s;
        if (c != null) {
            //clob 转 String
            s = c.getSubString(1, (int) c.length());
        } else if (b != null) {
            //blob 转 String
            s = new String(b.getBytes(1, (int) b.length()), "GBK");
        } else {
            s = "";
        }
        return s;
    }

}
