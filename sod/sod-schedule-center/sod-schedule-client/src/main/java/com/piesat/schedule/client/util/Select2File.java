
package com.piesat.schedule.client.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

@Slf4j
public class Select2File {



    public long expparttab2(String tableName,String path,StringBuilder sql,String parentId) throws Exception {
        PreparedStatement pre = null;
        byte[] signbytes = null;
        ResultSetMetaData rsmd = null;
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path),64 * 1024));
        ByteArrayOutputStream tempData = new ByteArrayOutputStream(64 * 1024);
        DataSourceContextHolder.setDataSource(parentId);
        DynamicDataSource dynamicDataSource=SpringUtil.getBean(DynamicDataSource.class);
        Connection con=null;
        long rows = 0L;
        try {
            con = dynamicDataSource.getConnection();
            DruidPooledPreparedStatement prepare = (DruidPooledPreparedStatement) con.prepareStatement(sql.toString());
            pre = prepare.getRawPreparedStatement();
            ((com.xugu.cloudjdbc.PreparedStatement)pre).setRecv_mode(1);
            int fnum = 0;
            boolean flag = true;
                ResultSet rs = pre.executeQuery();
                rsmd = rs.getMetaData();
                if(flag) {
                    fnum = rsmd.getColumnCount();
                    signbytes = new byte[(fnum + 3) / 4];
                }
                flag = false;
                int rowLen = 0;
                int idx = 0, off = 0;
                while (rs.next()) {
                    initBytes(signbytes);
                    rows++;
                    rowLen = signbytes.length;
                    for (int column = 1; column <= fnum; column++) {
                        byte[] fdata = null;
                        idx = (column - 1) / 4;
                        off = (column - 1) % 4;
                        if (rsmd.getColumnType(column) == Types.CLOB
                                || rsmd.getColumnType(column) == Types.NCLOB
                                || rsmd.getColumnType(column) == Types.BLOB
                                || rsmd.getColumnType(column) == Types.BINARY) {
                            fdata = rs.getBytes(column);
                        } else {
                            fdata = rs.getString(column) == null ? null: rs.getString(column).getBytes("UTF8");
                        }
                        if (fdata == null){
                            signbytes[idx] = (byte) (signbytes[idx] | SignByte.getNul(off));
                        } else if(fdata.length == 0){
                            signbytes[idx] = (byte) (signbytes[idx] | SignByte.getZero(off));
                        }  else if (fdata.length > Short.MAX_VALUE){
                            signbytes[idx] = (byte) (signbytes[idx] | SignByte.getLen(off));
                            tempData.write(Int2Bytes(fdata.length));
                            tempData.write(fdata);
                            rowLen += 4 + fdata.length;
                        } else if (fdata.length > 0){
                            tempData.write(Short2Bytes(fdata.length));
                            tempData.write(fdata);
                            rowLen += 2 + fdata.length;
                        }
                    }
                    dos.writeInt(rowLen);
                    dos.write(signbytes);
                    tempData.writeTo(dos);
                    tempData.flush();
                    tempData.reset();
                    if(rows%100000 == 0) {
                        log.info("表 "+tableName+" 已导出："+rows+" 条数据");
                    }
                }
                rs.close();
                rs = null;
                dos.flush();
            log.info("表 "+tableName+" 共导出："+rows+" 条数据");
        } catch (Exception e) {
            throw e;
        } finally {
            DataSourceContextHolder.clearDataSource();

            if (pre != null) {
                pre.close();
            }
            if(tempData != null) {
                tempData.close();
            }
            if(dos != null) {
                dos.close();
            }
            if(con!=null){
                con.close();
            }
        }
        return rows;
    }

    private byte[] initBytes(byte[] bbs) {
        for (int i = 0; i < bbs.length; i++) {
            bbs[i] = 0;
        }
        return bbs;
    }

    private byte[] Int2Bytes(int x) {
        byte[] bb = new byte[4];
        bb[0] = (byte) (x >> 24);
        bb[1] = (byte) (x >> 16);
        bb[2] = (byte) (x >> 8);
        bb[3] = (byte) (x >> 0);
        return bb;
    }

    private byte[] Short2Bytes(int x) {
        byte[] bb = new byte[2];
        bb[0] = (byte) (x >> 8);
        bb[1] = (byte) (x >> 0);
        return bb;
    }

    private enum SignByte {
        b0nul(0x01), b1nul(0x04), b2nul(0x10), b3nul(0x40), b0len(0x02), b1len(0x08), b2len(0x20), b3len(0x80),
        b0zero(0x03),b1zero(0x0c),b2zero(0x30),b3zero(0xc0);;
        private int val;

        private SignByte(int n) {
            this.val = n;
        }

        public static byte getLen(int i) {
            byte ret = 0x00;
            switch (i) {
                case 0:
                    ret = (byte) b0len.val;
                    break;
                case 1:
                    ret = (byte) b1len.val;
                    break;
                case 2:
                    ret = (byte) b2len.val;
                    break;
                case 3:
                    ret = (byte) b3len.val;
                    break;
            }
            return ret;
        }
        public static byte getZero(int i){
            byte ret = 0x00;
            switch (i)
            {
                case 0:
                    ret = (byte) b0zero.val;
                    break;
                case 1:
                    ret = (byte) b1zero.val;
                    break;
                case 2:
                    ret = (byte) b2zero.val;
                    break;
                case 3:
                    ret = (byte) b3zero.val;
                    break;
            }
            return ret;
        }

        public static byte getNul(int i) {
            byte ret = 0x00;
            switch (i) {
                case 0:
                    ret = (byte) b0nul.val;
                    break;
                case 1:
                    ret = (byte) b1nul.val;
                    break;
                case 2:
                    ret = (byte) b2nul.val;
                    break;
                case 3:
                    ret = (byte) b3nul.val;
                    break;
            }
            return ret;
        }
    }
//	public static void main(String[] args) throws Exception {
//
//	    @SuppressWarnings("resource")
//		DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName("com.xugu.cloudjdbc.Driver");
//        dataSource.setUsername("SYSDBA");
//        dataSource.setPassword("SYSDBA");
//        dataSource.setInitialSize(1);
//        dataSource.setMinIdle(1);
//        dataSource.setMaxActive(20);
//        //连接泄漏监测
//        dataSource.setRemoveAbandoned(true);
//        dataSource.setRemoveAbandonedTimeout(30);
//        //配置获取连接等待超时的时间
//        dataSource.setMaxWait(20000);
//        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
//        dataSource.setTimeBetweenEvictionRunsMillis(10000);
//        //防止过期
//        dataSource.setValidationQuery("SELECT 1");
//        dataSource.setTestWhileIdle(true);
//        dataSource.setTestOnBorrow(true);
//        dataSource.setUrl("jdbc:xugu://127.0.0.1:5138/system?recv_mode=0");
//        Connection con = dataSource.getConnection();
//		new Select2File().expparttab2("E:/test.exp", "test02",null,null, "*", "D_DATETIME|2019-10-08 00:00:00|2019-10-08 23:59:59",null,"UTF8", con,"HOUR","TEST");
//	}
}


