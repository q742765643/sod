package com.piesat.dm.common.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author cwh
 * @date 2020年 06月15日 14:16:07
 */
public class SmdbTest {
    static Properties pp = new Properties();

    public static DataSource getDataSource() throws Exception {
        pp.put("driverClassName", "com.xugu.cloudjdbc.Driver");
        pp.put("url", "jdbc:xugu://10.20.64.167:5139/BABJ_SMDB");
        pp.put("username", "USR_SOD");
        pp.put("password", "Pnmic_qwe123");
        return DruidDataSourceFactory.createDataSource(pp);
    }

    public static String subString(String str, String strStart, String strEnd) throws Exception {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            throw new Exception(str);
        }
        if (strEndIndex < 0) {
            throw new Exception(str);
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }


    public static void getIndexInfo(List<Map<String, String>> list, String path, String schema, Map<String, Map<String, String>> map) throws Exception {
        File file = new File(path);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                if (tempStr.startsWith("alter table") && tempStr.indexOf("INTEGER identity") == -1 && tempStr.indexOf("BIGINT identity") == -1) {
                    String type = "";
                    String tableName = subString(tempStr, "alter table ", " add");
                    String indexName = "";
                    String indexColumn = "";
                    if (tempStr.indexOf("primary key") != -1) {
                        type = "PK";
                        indexName = subString(tempStr, "add constraint \"", "\" primary key");
                        indexColumn = subString(tempStr, "primary key(\"", "\")").replaceAll("\"", "");
                    } else if (tempStr.indexOf("unique") != -1) {
                        type = "UK";
                        indexName = subString(tempStr, "add constraint \"", "\" unique");
                        indexColumn = subString(tempStr, "unique(\"", "\")").replaceAll("\"", "");
                    } else {
                        throw new Exception(tempStr);
                    }
                    Map<String, String> stringStringMap = map.get(tableName);
                    if (stringStringMap == null) {
                        System.out.println(tableName);
                        continue;
                    }
                    String ids = stringStringMap.get("IDS");
                    String[] split = ids.split(",");
                    for (String id : split) {
                        Map m = new HashMap();
                        m.put("tableName", tableName);
                        m.put("type", type);
                        m.put("indexName", indexName);
                        m.put("indexColumn", indexColumn);
                        m.put("id", id);
                        list.add(m);
                    }

                } else if (tempStr.startsWith("create index")) {

                    String tableName = subString(tempStr, "on " + schema + ".", "(\"");
                    String indexName = subString(tempStr, "create index \"", "\" on");
                    String indexColumn = subString(tempStr, "(\"", "\")").replaceAll("\"", "");
                    Map<String, String> stringStringMap = map.get(schema + "." + tableName);
                    if (stringStringMap == null) {
                        System.out.println(schema + "." + tableName);
                        continue;
                    }
                    String ids = stringStringMap.get("IDS");
                    String[] split = ids.split(",");
                    for (String id : split) {
                        Map m = new HashMap();
                        m.put("tableName", tableName);
                        m.put("type", "IDX");
                        m.put("indexName", indexName);
                        m.put("indexColumn", indexColumn);
                        m.put("id", id);
                        list.add(m);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }


    public static void insert(List<Map<String, String>> list) {
        Connection connection = null;
        Statement s = null;
        String sql = " INSERT INTO T_SOD_DATA_TABLE_INDEX (ID, CREATE_BY,DEL_FLAG,VERSION,INDEX_COLUMN,INDEX_NAME,INDEX_TYPE,TABLE_ID) VALUES (sys_guid(), 'admin',0,0,'%s','%s','%s','%s')";
        DataSource ds = null;
        try {
            ds = getDataSource();
            connection = ds.getConnection();
            s = connection.createStatement();
            for (Map<String, String> map : list) {
                String type = map.get("type");
                String indexName = map.get("indexName");
                String indexColumn = map.get("indexColumn");
                String id = map.get("id");
                String format = String.format(sql, indexColumn, indexName, type, id);
                s.executeQuery(format);
            }
            s.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }


    }

    public static void main(String[] args) {
        Connection connection = null;
        Statement s = null;
        String path1 = "C:\\Users\\cuiwenhui\\Desktop\\sql\\BABJ_STDB.sql";
        String path2 = "C:\\Users\\cuiwenhui\\Desktop\\sql\\ARTPAD.sql";
        String path3 = "C:\\Users\\cuiwenhui\\Desktop\\sql\\CIPAS.sql";
        String path4 = "C:\\Users\\cuiwenhui\\Desktop\\sql\\DATAQC.sql";
        String path5 = "C:\\Users\\cuiwenhui\\Desktop\\sql\\DQASS.sql";
        String path6 = "C:\\Users\\cuiwenhui\\Desktop\\sql\\WEANET.sql";
//        String path7 = "C:\\Users\\cuiwenhui\\Desktop\\sql\\WMC.sql";

        List<Map<String, String>> list1 = new ArrayList();
        List<Map<String, String>> list2 = new ArrayList();
        List<Map<String, String>> list3 = new ArrayList();
        List<Map<String, String>> list4 = new ArrayList();
        List<Map<String, String>> list5 = new ArrayList();
        List<Map<String, String>> list6 = new ArrayList();
//        List list7 = new ArrayList();


        try {
            Map<String, Map<String, String>> tableInfo = new HashMap<>();
            DataSource ds = getDataSource();
            connection = ds.getConnection();
            s = connection.createStatement();
            String sql = "select b.SCHEMA_NAME,c.TABLE_NAME,wm_concat(c.id) IDS from T_SOD_DATA_LOGIC a inner join T_SOD_DATABASE b on a.DATABASE_ID = b.id " +
                    "inner join T_SOD_DATA_TABLE c on a.id = c.CLASS_LOGIC_ID  group by b.SCHEMA_NAME,c.TABLE_NAME";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Map<String, String> m = new HashMap<>();
                String SCHEMA_NAME = rs.getObject("SCHEMA_NAME").toString();
                String TABLE_NAME = rs.getObject("TABLE_NAME").toString();
                String IDS = rs.getObject("IDS").toString();
                m.put("SCHEMA_NAME", SCHEMA_NAME);
                m.put("TABLE_NAME", TABLE_NAME);
                m.put("IDS", IDS);
                tableInfo.put(SCHEMA_NAME + "." + TABLE_NAME, m);
            }
            s.close();
            connection.close();
            getIndexInfo(list1, path1, "USR_SOD", tableInfo);
            getIndexInfo(list2, path2, "ARTPAD", tableInfo);
            getIndexInfo(list3, path3, "CIPAS", tableInfo);
            getIndexInfo(list4, path4, "DATAQC", tableInfo);
            getIndexInfo(list5, path5, "DQASS", tableInfo);
            getIndexInfo(list6, path6, "WEANET", tableInfo);
            insert(list1);
            insert(list2);
            insert(list3);
            insert(list4);
            insert(list5);
            insert(list6);
        } catch (Exception e) {
            e.printStackTrace();
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

}
