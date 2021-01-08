package com.piesat.dm.core;

import com.alibaba.fastjson.JSON;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.datasource.CommDataSource;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.factory.AuzDatabase;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.util.ResultT;
import org.apache.commons.collections.MapUtils;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月07日 18:59:26
 */
public class ExportMateApp {

    /**
     * Description
     */

    private static String des = "-- 执行此sql文件会删除现有四级编码[%s]下的存储表结构相关信息，确认后操作 --";
    /**
     * INSERT
     */
    private static String T_SOD_DATA_CLASS = "select * from USR_SOD.T_SOD_DATA_CLASS where D_DATA_ID = '%s'";
    private static String T_SOD_DATA_LOGIC = "select * from USR_SOD.T_SOD_DATA_LOGIC where DATA_CLASS_ID in (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID = '%s')";

    private static String T_SOD_DATA_TABLE = "select * from USR_SOD.T_SOD_DATA_TABLE where CLASS_LOGIC_ID in ( select id from T_SOD_DATA_LOGIC where DATA_CLASS_ID in (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s'))";
    private static String T_SOD_DATA_TABLE_FOREIGN_KEY = "select * from USR_SOD.T_SOD_DATA_TABLE_FOREIGN_KEY where CLASS_LOGIC_ID in ( select id from T_SOD_DATA_LOGIC where DATA_CLASS_ID in (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s'))";
    private static String T_SOD_DATA_TABLE_COLUMN = "select * from USR_SOD.T_SOD_DATA_TABLE_COLUMN where TABLE_ID in (select id from T_SOD_DATA_TABLE where CLASS_LOGIC_ID in ( select id from T_SOD_DATA_LOGIC where DATA_CLASS_ID in (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s')))";
    private static String T_SOD_DATA_TABLE_INDEX = "select * from USR_SOD.T_SOD_DATA_TABLE_INDEX where TABLE_ID in (select id from T_SOD_DATA_TABLE where CLASS_LOGIC_ID in ( select id from T_SOD_DATA_LOGIC where DATA_CLASS_ID in (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s')))";
    private static String T_SOD_DATA_TABLE_SHARDING = "select * from USR_SOD.T_SOD_DATA_TABLE_SHARDING where TABLE_ID in (select id from T_SOD_DATA_TABLE where CLASS_LOGIC_ID in ( select id from T_SOD_DATA_LOGIC where DATA_CLASS_ID in (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s')))";


    private static String T_SOD_DATACLASS_NORM = "select * from USR_SOD.T_SOD_DATACLASS_NORM where id in  (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s')";
    private static String T_SOD_GRID_DECODING = "select * from USR_SOD.T_SOD_GRID_DECODING where data_service_id in  (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s')";
    private static String T_SOD_GRID_AREA = "select * from USR_SOD.T_SOD_GRID_AREA where data_service_id in  (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s')";
    private static String T_SOD_DATASERVICE_BASEINFOR = "select * from USR_SOD.T_SOD_DATASERVICE_BASEINFOR where data_class_id in  (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s')";
    private static String T_SOD_DATASERVICE_CONFIG = "select * from USR_SOD.T_SOD_DATASERVICE_CONFIG where data_service_id in  (select DATA_CLASS_ID from T_SOD_DATA_CLASS where D_DATA_ID  = '%s')";


    /**
     * DELETE
     */
    private static String DEL_SQL = "DELETE FROM USR_SOD.T_SOD_DATA_TABLE_COLUMN WHERE TABLE_ID IN (SELECT ID FROM T_SOD_DATA_TABLE WHERE CLASS_LOGIC_ID IN ( SELECT ID FROM T_SOD_DATA_LOGIC WHERE DATA_CLASS_ID IN (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s')));\n"
            + "DELETE FROM USR_SOD.T_SOD_DATA_TABLE_INDEX WHERE TABLE_ID IN (SELECT ID FROM T_SOD_DATA_TABLE WHERE CLASS_LOGIC_ID IN ( SELECT ID FROM T_SOD_DATA_LOGIC WHERE DATA_CLASS_ID IN (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s')));\n"
            + "DELETE FROM USR_SOD.T_SOD_DATA_TABLE_SHARDING WHERE TABLE_ID IN (SELECT ID FROM T_SOD_DATA_TABLE WHERE CLASS_LOGIC_ID IN ( SELECT ID FROM T_SOD_DATA_LOGIC WHERE DATA_CLASS_ID IN (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s')));\n"
            + "DELETE FROM USR_SOD.T_SOD_DATA_TABLE WHERE CLASS_LOGIC_ID IN ( SELECT ID FROM T_SOD_DATA_LOGIC WHERE DATA_CLASS_ID IN (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s'));\n"
            + "DELETE FROM USR_SOD.T_SOD_DATA_TABLE_FOREIGN_KEY WHERE CLASS_LOGIC_ID IN ( SELECT ID FROM T_SOD_DATA_LOGIC WHERE DATA_CLASS_ID IN (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s'));\n"
            + "DELETE FROM USR_SOD.T_SOD_DATACLASS_NORM WHERE ID IN  (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s');\n"
            + "DELETE FROM USR_SOD.T_SOD_GRID_DECODING WHERE DATA_SERVICE_ID IN  (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s');\n"
            + "DELETE FROM USR_SOD.T_SOD_GRID_AREA WHERE DATA_SERVICE_ID IN  (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s');\n"
            + "DELETE FROM USR_SOD.T_SOD_DATASERVICE_BASEINFOR WHERE DATA_CLASS_ID IN  (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s');\n"
            + "DELETE FROM USR_SOD.T_SOD_DATASERVICE_CONFIG WHERE DATA_SERVICE_ID IN  (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID  = '%s');\n"
            + "DELETE FROM USR_SOD.T_SOD_DATA_LOGIC WHERE DATA_CLASS_ID IN (SELECT DATA_CLASS_ID FROM T_SOD_DATA_CLASS WHERE D_DATA_ID = '%s');\n"
            + "DELETE FROM USR_SOD.T_SOD_DATA_CLASS WHERE D_DATA_ID = '%s';\n";


    private static String[] arr;

    private static String input = "D:\\file\\input.txt";

    public static void main(String[] args) throws SQLException, IOException {

        arr = toArrayByFileReader();
        ConnectVo c = new ConnectVo();
        c.setUrl("jdbc:xugu://10.20.64.168:5138/BABJ_SMDB?ips=10.20.64.167,10.20.64.169&recv_mode=0");
        c.setPort(5138);
        c.setClassName("com.xugu.cloudjdbc.Driver");
        c.setUserName("usr_sod");
        c.setPassWord("Pnmic_qwe123");

        Connection smdb = CommDataSource.getConnection("SMDB", c);
        AuzDatabase a = new AuzDatabase(DatabaseTypesEnum.XUGU, smdb);
        ResultT r = new ResultT();
        List<String> sqlList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (StringUtils.isEmpty(arr[i])) {
                continue;
            }
            sqlList.add(DEL_SQL.replaceAll("%s", arr[i]));

            List<Map<String, Object>> m1 = a.exeQuery(String.format(T_SOD_DATA_CLASS, arr[i]), r);
            sqlList.addAll(getSql(m1, "USR_SOD.T_SOD_DATA_CLASS"));
            List<Map<String, Object>> m2 = a.exeQuery(String.format(T_SOD_DATA_LOGIC, arr[i]), r);
            sqlList.addAll(getSql(m2, "USR_SOD.T_SOD_DATA_LOGIC"));

            List<Map<String, Object>> m3 = a.exeQuery(String.format(T_SOD_DATA_TABLE, arr[i]), r);
            sqlList.addAll(getSql(m3, "USR_SOD.T_SOD_DATA_TABLE"));
            List<Map<String, Object>> m4 = a.exeQuery(String.format(T_SOD_DATA_TABLE_FOREIGN_KEY, arr[i]), r);
            sqlList.addAll(getSql(m4, "USR_SOD.T_SOD_DATA_TABLE_FOREIGN_KEY"));

            List<Map<String, Object>> m5 = a.exeQuery(String.format(T_SOD_DATA_TABLE_COLUMN, arr[i]), r);
            sqlList.addAll(getSql(m5, "USR_SOD.T_SOD_DATA_TABLE_COLUMN"));

            List<Map<String, Object>> m6 = a.exeQuery(String.format(T_SOD_DATA_TABLE_INDEX, arr[i]), r);
            sqlList.addAll(getSql(m6, "USR_SOD.T_SOD_DATA_TABLE_INDEX"));

            List<Map<String, Object>> m7 = a.exeQuery(String.format(T_SOD_DATA_TABLE_SHARDING, arr[i]), r);
            sqlList.addAll(getSql(m7, "USR_SOD.T_SOD_DATA_TABLE_SHARDING"));

            List<Map<String, Object>> m8 = a.exeQuery(String.format(T_SOD_DATACLASS_NORM, arr[i]), r);
            sqlList.addAll(getSql(m8, "USR_SOD.T_SOD_DATACLASS_NORM"));

            List<Map<String, Object>> m9 = a.exeQuery(String.format(T_SOD_GRID_DECODING, arr[i]), r);
            sqlList.addAll(getSql(m9, "USR_SOD.T_SOD_GRID_DECODING"));

            List<Map<String, Object>> m10 = a.exeQuery(String.format(T_SOD_GRID_AREA, arr[i]), r);
            sqlList.addAll(getSql(m10, "USR_SOD.T_SOD_GRID_AREA"));

            List<Map<String, Object>> m11 = a.exeQuery(String.format(T_SOD_DATASERVICE_BASEINFOR, arr[i]), r);
            sqlList.addAll(getSql(m11, "USR_SOD.T_SOD_DATASERVICE_BASEINFOR"));

            List<Map<String, Object>> m12 = a.exeQuery(String.format(T_SOD_DATASERVICE_CONFIG, arr[i]), r);
            sqlList.addAll(getSql(m12, "USR_SOD.T_SOD_DATASERVICE_CONFIG"));

            toFile(sqlList, arr[i]);
            sqlList.clear();
        }
        a.close();
    }

    public static void toFile(List<String> sqlList, String dated) throws IOException {
        String date = DateUtils.dateTime();
        String path = "D:\\file\\存储元数据导出\\" + date;
        File file = new File(path);
        //判断文件夹是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        // 脚本名称
        file = new File(path + "\\" + dated + "_" + date + ".sql");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write(String.format(des, dated));
        bufferedWriter.newLine();
        bufferedWriter.newLine();
        bufferedWriter.newLine();
        for (int i = 0; i < sqlList.size(); i++) {
            bufferedWriter.write(sqlList.get(i));
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();  //如果用于网络传输，记得强制刷新缓冲区，否则输出的数据只停留在缓冲区中，而无法进行网络传输
        bufferedWriter.close();  // 关闭输出流  文件不存在会自动创建
        outputStreamWriter.close(); // 关闭资源
    }

    public static List<String> getSql(List<Map<String, Object>> maps, String tableName) throws UnsupportedEncodingException, SQLException {
        List<String> l = new ArrayList<>();
        for (int i1 = 0; i1 < maps.size(); i1++) {
            String sql = genSqlInsert(tableName, maps.get(i1));
            l.add(sql);
        }
        return l;
    }

    /**
     * 通过Map拼接Insert SQL语句
     *
     * @param tableName
     * @param dataMap
     * @return
     */
    public static String genSqlInsert(String tableName, Map<String, Object> dataMap) throws UnsupportedEncodingException, SQLException {
        if (MapUtils.isEmpty(dataMap)) {
            return null;
        }
        //生成INSERT INTO table(field1,field2) 部分
        StringBuffer sbField = new StringBuffer();
        //生成VALUES('value1','value2') 部分
        StringBuffer sbValue = new StringBuffer();

        sbField.append("INSERT INTO " + tableName.toUpperCase() + "(");

        List<String> s = new ArrayList<>();
        List<String> v = new ArrayList<>();

        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            s.add("`" + mapKey + "`");

            if (mapValue instanceof Blob) {
                mapValue = getString(null, (Blob) mapValue);
            } else if (mapValue instanceof Clob) {
                mapValue = getString((Clob) mapValue, null);
            }

            if (mapValue == null || mapValue instanceof BigDecimal || mapValue instanceof Boolean) {
                v.add(String.valueOf(mapValue));
            } else {
                v.add("'" + mapValue + "'");
            }

        }
        return sbField.toString() + s.stream().collect(Collectors.joining(","))
                + ") VALUES(" + v.stream().collect(Collectors.joining(",")) + ");\n";
    }


    public static String getString(Clob c, Blob b) throws SQLException, UnsupportedEncodingException {
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


    public static String[] toArrayByFileReader() {
        // 使用ArrayList来存储每行读取到的字符串
        List<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(input);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] objects = (String[]) arrayList.stream()
                .distinct()
                .map(String::trim)
                .toArray(String[]::new);
        return objects;
    }
}
