package com.piesat.dm.core.api.impl;

import com.piesat.dm.common.util.TemplateUtil;
import com.piesat.dm.core.api.AbstractDatabaseDcl;
import com.piesat.util.ResultT;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 虚谷数据库管理
 *
 * @author cwh
 * @date 2020年 02月04日 15:50:07
 */
public class Xugu extends AbstractDatabaseDcl {

    public static final String DRIVER = "com.xugu.cloudjdbc.Driver";
    public static final String ADD_USER_SQL = "CREATE USER ${userName} IDENTIFIED BY '${password}'";
    public static final String GET_USER_NUM_SQL = "SELECT COUNT(*) FROM DBA_USERS WHERE  UPPER(USER_NAME) = '${userName}'";
    public static final String ADD_USER_GRANT_SQL = "GRANT CREATE ANY SCHEMA TO ${userName} ";
    public static final String DROP_USER_SQL = "DROP USER ${userName} ";
    public static final String LOCK_USER_SQL = "ALTER USER ${userName} ACCOUNT LOCK ";


    public Xugu(String url, String user, String password) throws Exception {
        try {
            Class.forName(DRIVER);
            DriverManager.setLoginTimeout(5);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("虚谷数据库连接失败");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("虚谷数据驱动类缺失");
        }
    }

    @Override
    public void addUser(String identifier, String password, String[] ips) throws Exception {
        int userNum = getUserNum(identifier);
        if (userNum > 0) {
            throw new Exception("数据库用户已存在！");
        }
        Map<String, Object> model = new HashMap<>();
        model.put(USER_NAME, identifier);
        model.put(PASSWORD, password);
        String addUserSql = TemplateUtil.rendering(ADD_USER_SQL, model);
        String addUserGrantSql = TemplateUtil.rendering(ADD_USER_GRANT_SQL, model);

        try {
            stmt = connection.createStatement();
            stmt.execute(addUserSql);
            stmt.execute(addUserGrantSql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("新增用户失败！ERRINFO：" + e.getMessage());
        }

    }

    @Override
    public int getUserNum(String user) throws Exception {
        int num = 0;
        Map<String, Object> model = new HashMap<>();
        model.put(USER_NAME, user.toUpperCase());
        String sql = TemplateUtil.rendering(GET_USER_NUM_SQL, model);
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                num = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
        }
        return num;
    }

    @Override
    public void deleteUser(String identifier, String ip) throws Exception {
        int userNum = getUserNum(identifier);
        Map<String, Object> model = new HashMap<>();
        model.put(USER_NAME, identifier);
        try {
            if (userNum > 0) {
                String dropUserSql = TemplateUtil.rendering(DROP_USER_SQL, model);
                stmt = connection.createStatement();
                stmt.execute(dropUserSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("删除用户失败！errInfo：" + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String identifier) throws Exception {
        int userNum = getUserNum(identifier);
        try {
            if (userNum > 0) {
                Map<String, Object> model = new HashMap<>();
                model.put(USER_NAME, identifier);
                String dropUserSql = TemplateUtil.rendering(DROP_USER_SQL, model);
                stmt = connection.createStatement();
                stmt.execute(dropUserSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("删除用户失败！errInfo：" + e.getMessage());
        }
    }


    public void disabledUser(String identifier) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put(USER_NAME, identifier);
        String lockUserSql = TemplateUtil.rendering(LOCK_USER_SQL, model);
        try {
            stmt = connection.createStatement();
            stmt.execute(lockUserSql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("禁用用户(" + identifier + ")失败！errInfo：" + e.getMessage());
        }
    }

    @Override
    public void addEnable(String identifier, String resource, List<String> ips, int type) {

    }

    @Override
    public void deleteEnable(String identifier, String resource, List<String> ips, int type) {

    }

    @Override
    public void addPermissions(Boolean select, String resource, String tableName, String identifier, String password, List<String> ips) throws SQLException {
        String permission = select ? SELECT_GRANT : ALL_GRANT;
        String sql = "GRANT " + permission + " ON " + resource + "." + tableName + " To " + identifier;
        stmt = connection.createStatement();
        stmt.execute(sql);
    }

    @Override
    public void deletePermissions(String[] permissions, String resource, String tableName, String identifier, String password, List<String> ips) throws Exception {
        String permission = Arrays.stream(permissions).collect(Collectors.joining(","));
        String sql = "REVOKE " + permission + " ON " + resource + "." + tableName + " FROM " + identifier;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("撤销数据库授权失败！errInfo：" + e.getMessage());
        }
    }

    @Override
    public void createSchemas(String schemaName, String dataBaseUser, String password, boolean dataAuthor, boolean creatAuthor, boolean dropAuthor, List<String> ips) throws Exception {
        String sql = "SELECT COUNT(*) FROM DBA_SCHEMAS WHERE SCHEMA_NAME = '" + schemaName + "'";
        int num = 0;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                num = rs.getInt(1);
            }
            if (num == 0 && !schemaName.equals(dataBaseUser)) {
                sql = "CREATE SCHEMA " + schemaName + " AUTHORIZATION " + dataBaseUser;
                stmt = connection.createStatement();
                stmt.execute(sql);
            }
            if (dataAuthor) {
                //数据修改权限
                stmt = connection.createStatement();
                String[] as = {"SELECT", "INSERT", "DELETE", "UPDATE"};
                for (int i = 0; i < as.length; i++) {
                    sql = "GRANT " + as[i] + " ANY TABLE IN SCHEMA " + schemaName + " TO " + dataBaseUser;
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
            }
            if (creatAuthor) {
                //建表权限
                stmt = connection.createStatement();
                String[] as = {"CREATE", "ALTER"};
                for (int i = 0; i < as.length; i++) {
                    sql = "GRANT " + as[i] + " ANY TABLE IN SCHEMA " + schemaName + " TO " + dataBaseUser;
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
                String createSql = "CREATE TABLE " + schemaName + ".TEST (id INT identity(1,1),name CHAR (10),sex CHAR (2),age INTEGER,address VARCHAR,tel INTEGER);";
                this.createTable(createSql, schemaName + ".TEST", true);
            }
            if (dropAuthor) {
                //删表权限
                stmt = connection.createStatement();
                String[] as = {"DROP"};
                for (int i = 0; i < as.length; i++) {
                    sql = "GRANT " + as[i] + " ANY TABLE IN SCHEMA " + schemaName + " TO " + dataBaseUser;
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
        }

    }

    @Override
    public void dropSchemas(String schemaName) throws Exception {
        String sql = "SELECT COUNT(*) FROM DBA_SCHEMAS WHERE SCHEMA_NAME = '" + schemaName + "'";
        int num = 0;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                num = rs.getInt(1);
            }
            if (num > 0) {
                sql = "DROP SCHEMA " + schemaName;
                stmt = connection.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
        }
    }

    @Override
    public ResultT createTable(String sql, String tableName, Boolean deleteOld) {
        try {
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        try {
            String delSql = "DROP TABLE " + tableName;
            stmt.execute(delSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String[] sqlList = sql.split(";");
            for (String s : sqlList) {
                if (StringUtils.isNotBlank(s)){
                    stmt.execute(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success();
    }

    @Override
    public ResultT existTable(String schema, String tableName) {
        String sql = "SELECT * FROM " + schema + "." + tableName + " LIMIT 10";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            int num = 0;
            while (rs.next()) {
                num++;
            }
            if (num > 0) {
                return ResultT.success(true);
            } else {
                return ResultT.success(false);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("表或视图") && e.getMessage().contains("不存在")) {
                return ResultT.success(false);
            } else {
                e.printStackTrace();
                return ResultT.failed(e.getMessage());
            }
        }
    }

    @Override
    public ResultT updateAccount(String dataBaseUser, String newPassword) {
        String delSql = "ALTER USER " + dataBaseUser + " IDENTIFIED BY '" + newPassword + "'";
        try {
            stmt = connection.createStatement();
            stmt.execute(delSql);
        } catch (SQLException e) {
            e.printStackTrace();
            if (!e.getMessage().contains("新口令不能与老口令相同")) {
                return ResultT.failed(e.getMessage());
            }
        }
        return ResultT.success();
    }

    @Override
    public ResultT queryAllTableName(String schema) throws Exception {
        List<String> list = new ArrayList<String>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getTables(null, schema, null, new String[]{"TABLE"});
            while (rs.next()) {
                if (!rs.getString("TABLE_NAME").contains("#")) {
                    list.add(rs.getString("TABLE_NAME"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success(list);
    }

    @Override
    public ResultT queryAllColumnInfo(String schema, String tableName) throws Exception {
        Map<String, Map<String, Object>> columnInfos = new HashMap<String, Map<String, Object>>();
        String sql = "select  table_schema,table_name,upper(column_name) column_name,column_type,is_nullable,column_comment" +
                " from information_schema.columns where TABLE_NAME= '" + tableName + "' and table_schema='" + schema + "'";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map columnOneInfo = new HashMap<String, Object>();
                columnOneInfo.put("column_name", rs.getString("column_name"));
                columnOneInfo.put("column_type", rs.getString("column_type"));//类型和精度 decimal(4,0)  varchar(200)
                columnOneInfo.put("column_comment", rs.getString("column_comment"));//字段含义
                if ("NO".equals(rs.getString("is_nullable"))) {
                    columnOneInfo.put("is_nullable", 0);
                } else {
                    columnOneInfo.put("is_nullable", 1);
                }
                columnInfos.put(rs.getString("column_name"), columnOneInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }

        }
        return ResultT.success(columnInfos);
    }

    @Override
    public ResultT queryAllIndexAndShardingInfo(String schema, String tableName) throws Exception {
        HashMap<String, String> indexs = new HashMap<String, String>();
        HashMap<String, String> shardings = new HashMap<String, String>();
        Map<String, Map<String, String>> results = new HashMap<String, Map<String, String>>();
        String sql = "select * from information_schema.index where table_name='" + tableName + "' and schema_name='" + schema + "'";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("index_name").startsWith("auto")) {
                    shardings.put(rs.getString("index_name"), rs.getString("index_column"));
                } else {
                    indexs.put(rs.getString("index_name"), rs.getString("index_column"));
                }
            }
            results.put("indexs", indexs);
            results.put("shardings", shardings);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }

        }
        return ResultT.success(results);
    }

    @Override
    public void bindIp(String identifier, String[] ips) throws Exception {

    }

    @Override
    public String queryRecordNum(String schema, String tableName) throws Exception {
        String num = "";
        String sql = "SELECT COUNT(*) as COUNT FROM " + schema + "." + tableName;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                num = rs.getString("COUNT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }

        }
        return num;
    }

    @Override
    public String queryMinTime(String schema, String tableName, String timeColumnName) throws Exception {
        String minTime = "";
        // 获取所有分区号
        String sql = "SELECT PARTI_VAL FROM DBA_PARTIS WHERE TABLE_ID=(SELECT TABLE_ID FROM DBA_TABLES A INNER JOIN DBA_SCHEMAS B ON A.SCHEMA_ID=B.SCHEMA_ID WHERE TABLE_NAME='"
                + tableName.toUpperCase() + "' AND SCHEMA_NAME='" + schema.toUpperCase() + "') order by parti_no";
        List<String> parti_val = new ArrayList<String>();
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                parti_val.add(rs.getString(1));
            }
            rs.close();
            //表没有分区，不是分区表
            if (parti_val.size() == 0) {
                sql = "SELECT MIN(" + timeColumnName + ") FROM " + schema + "." + tableName;
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    minTime = rs.getString(1);
                }
            } else {
                // 获取首分区键值，判断是否存在数据，否则偏移至第二个分区
                sql = "SELECT MIN(" + timeColumnName + ") FROM " + schema + "." + tableName + " WHERE " + timeColumnName + "<" + parti_val.get(0) + "";
                rs = stmt.executeQuery(sql);
                if (rs.next() && rs.getString(1) != null) {
                    minTime = rs.getString(1);
                } else {
                    for (int i = 0; i < parti_val.size() - 1; i++) {
                        //获取偏移分区间的最小值
                        sql = "SELECT MIN(" + timeColumnName + ") FROM " + schema + "." + tableName + " WHERE " + timeColumnName + ">=" + parti_val.get(i) + " AND " + timeColumnName + "<" + parti_val.get(i + 1);
                        rs = stmt.executeQuery(sql);
                        if (!rs.next()) {
                            rs.close();
                            continue;
                        }
                        minTime = rs.getString(1);
                        if (minTime == null) {
                            rs.close();
                            continue;
                        } else {
                            break;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }

        }
        return minTime;
    }

    @Override
    public String queryMaxTime(String schema, String tableName, String timeColumnName) throws Exception {
        String maxTime = "";
        // 获取所有分区号
        String sql = "SELECT PARTI_VAL FROM DBA_PARTIS WHERE TABLE_ID=(SELECT TABLE_ID FROM DBA_TABLES A INNER JOIN DBA_SCHEMAS B ON A.SCHEMA_ID=B.SCHEMA_ID WHERE UPPER(TABLE_NAME)='"
                + tableName.toUpperCase() + "' AND SCHEMA_NAME='" + schema.toUpperCase() + "') order by parti_no DESC";
        List<String> parti_val = new ArrayList<String>();
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                parti_val.add(rs.getString(1));
            }
            rs.close();
            //表没有分区，不是分区表
            if (parti_val.size() == 0) {
                sql = "SELECT MAX(" + timeColumnName + ") FROM " + schema + "." + tableName;
                rs = stmt.executeQuery(sql);
                if (rs.next() && rs.getString(1) != null) {
                    maxTime = rs.getString(1);
                }
            } else {
                for (int i = 0; i < parti_val.size() - 1; i++) {
                    //获取偏移分区间的最小值
                    sql = "SELECT MAX(" + timeColumnName + ") FROM " + schema + "." + tableName + " WHERE " + timeColumnName + ">=" + parti_val.get(i + 1) + " AND " + timeColumnName + "<" + parti_val.get(i);
                    rs = stmt.executeQuery(sql);
                    if (!rs.next()) {
                        rs.close();
                        continue;
                    }
                    maxTime = rs.getString(1);
                    if (maxTime == null) {
                        rs.close();
                        continue;
                    } else {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }

        }
        return maxTime;
    }

    @Override
    public String queryIncreCount(String schema, String tableName, String timeColumnName, String beginTime, String endTime) throws Exception {
        String num = "";
        String sql = "SELECT COUNT(*) as COUNT FROM " + schema + "." + tableName + " WHERE " + timeColumnName + ">='" + beginTime + "' AND " + timeColumnName + "<'" + endTime + "'";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                num = rs.getString("COUNT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }

        }
        return num;
    }

}
