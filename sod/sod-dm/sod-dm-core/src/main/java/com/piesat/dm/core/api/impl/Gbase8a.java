package com.piesat.dm.core.api.impl;

import com.piesat.dm.core.api.AbstractDatabaseDcl;
import com.piesat.util.ResultT;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 8a数据库管理
 *
 * @author cwh
 * @date 2020年 02月04日 16:11:06
 */
public class Gbase8a extends AbstractDatabaseDcl {
    private final String driver = "com.gbase.jdbc.Driver";

    public Gbase8a(String url, String user, String password) throws Exception {
        try {
            Class.forName(driver);
            DriverManager.setLoginTimeout(5);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Gbase8a数据库连接失败");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Gbase8a数据驱动类缺失");
        }
    }


    @Override
    public int getUserNum(String user) throws Exception {
        int num = 0;
        String sql = "SELECT COUNT(*) FROM GBASE.USER WHERE USER='" + user + "'";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                num = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new Exception("错误：" + e.getMessage());
        }
        return num;
    }

    @Override
    public void addUser(String identifier, String password, String[] ips) throws Exception {
        int userNum = getUserNum(identifier);
        if (userNum > 0) {
            throw new Exception("数据库用户已存在！");
        }
        String ipStr = StringUtils.join(ips, " ");

//            String sql = "CREATE USER '" + identifier + "'@'" + ip + "' IDENTIFIED BY '" + password + "'";
        String sql = "create user '" + identifier + "' identified by '" + password + "' hosts '" + ipStr + "'";
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new Exception("新增用户失败！errInfo：" + e.getMessage());
        }

    }

    @Override
    public void deleteUser(String identifier, String ip) throws Exception {
        String sql = "SELECT HOST FROM GBASE.USER WHERE USER ='" + identifier + "'";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String host = rs.getString("HOST").trim();
                if (host.equals(ip)) {
                    sql = "DROP USER ?@?";
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, identifier);
                    ps.setString(2, host);
                    ps.executeUpdate();
                }

            }
        } catch (SQLException e) {
            throw new Exception("删除用户失败！errInfo：" + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String identifier) throws Exception {
        int userNum = getUserNum(identifier);
        if (userNum == 0) {
            return;
        }
        String sql = "DROP USER  " + identifier;
        stmt = connection.createStatement();
        stmt.execute(sql);
    }

    @Override
    public void addEnable(String identifier, String resource, List<String> ips, int type) {

    }

    @Override
    public void deleteEnable(String identifier, String resource, List<String> ips, int type) {

    }

    @Override
    public void addPermissions(Boolean select, String resource, String tableName, String identifier, String password, List<String> ips) throws Exception {
        String permission = select ? "SELECT" : "SELECT,UPDATE,INSERT,DELETE";
        stmt = connection.createStatement();
        String sql = "GRANT " + permission + " ON " + resource + "." + tableName + " To " + identifier;
        stmt.execute(sql);
    }

    @Override
    public void deletePermissions(String[] permissions, String resource, String tableName, String identifier, String password, List<String> ips) throws Exception {
        String permission = Arrays.stream(permissions).collect(Collectors.joining(","));
        try {
            stmt = connection.createStatement();
            String sql = "REVOKE " + permission + " ON " + resource + "." + tableName + " FROM '" + identifier;
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new Exception("撤销数据库授权失败！errInfo：" + e.getMessage());
        }
    }

    @Override
    public void createSchemas(String schemaName, String dataBaseUser, String password, boolean dataAuthor, boolean creatAuthor, boolean dropAuthor, List<String> ips) throws Exception {
        String sql = "SELECT COUNT(SCHEMA_NAME) from INFORMATION_SCHEMA.SCHEMATA where SCHEMA_NAME = '" + schemaName + "'";
        int num = 0;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                num = rs.getInt(1);
            }
            if (num == 0) {
                sql = "CREATE DATABASE IF NOT EXISTS " + schemaName;
                ps = connection.prepareStatement(sql);
                ps.executeUpdate();
            }
            //permissions[0]:数据权限，permissions[1]:建表权限，permissions[2]:删表权限
            String[] permissions = {"SELECT,UPDATE,INSERT,DELETE", "CREATE,ALTER", "DROP"};

            stmt = connection.createStatement();

            for (int j = 0; j < permissions.length; j++) {
                sql = "GRANT " + permissions[j] + " ON " + schemaName + ".* to " + dataBaseUser;
                if ((dataAuthor && j == 0) || (creatAuthor && j == 1) || (dropAuthor && j == 2)) {
                    stmt.execute(sql);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
        }

    }

    @Override
    public void dropSchemas(String schemaName) {

    }

    @Override
    public ResultT createTable(String sql, String tableName, Boolean deleteOld) {
        try {
            stmt = connection.createStatement();
            String delSql = "DROP TABLE IF EXISTS " + tableName;
            stmt.execute(delSql);
            String[] sqlList = sql.split(";");
            for (String s : sqlList) {
                stmt.execute(s);
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
            e.printStackTrace();
            if (e.getMessage().contains("doesn't exist")) {
                return ResultT.success(false);
            } else {
                e.printStackTrace();
                return ResultT.failed(e.getMessage());
            }
        }
    }

    @Override
    public ResultT updateAccount(String dataBaseUser, String newPassword) {
        String sql = " SET PASSWORD FOR " + dataBaseUser + " = PASSWORD('" + newPassword + "')";
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            if (!e.getMessage().contains("Can't find any matching row in the user table")) {
                return ResultT.failed(e.getMessage());
            }
        }
        return ResultT.success();
    }

    @Override
    public ResultT queryAllTableName(String schema) throws Exception {
        List<String> list = new ArrayList<String>();
        String sql = "select table_name from information_schema.tables where table_schema='" + schema + "'";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString("TABLE_NAME").contains("#")) {
                    list.add(rs.getString("TABLE_NAME"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success(list);
    }

    @Override
    public ResultT queryAllColumnInfo(String schema, String tableName) throws Exception {
        Map<String, Map<String, Object>> columnInfos = new HashMap<String, Map<String, Object>>();
        String sql = "select  table_schema,table_name,upper(column_name) column_name,data_type column_type,is_nullable,column_comment" +
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
        }
        return ResultT.success(columnInfos);
    }

    @Override
    public ResultT queryAllIndexAndShardingInfo(String schema, String tableName) throws Exception {
        HashMap<String, String> indexs = new HashMap<String, String>();
        HashMap<String, String> shardings = new HashMap<String, String>();
        Map<String, Map<String, String>> results = new HashMap<String, Map<String, String>>();
        String sql = "select table_name,non_unique,index_schema,index_name,GROUP_CONCAT(column_name) as index_column from information_schema.STATISTICS " +
                " WHERE TABLE_NAME='" + tableName + "' and index_schema='" + schema + "'" +
                " group by table_name,non_unique,index_schema,index_name;";
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
        }
        return ResultT.success(results);
    }

    @Override
    public void bindIp(String identifier, String[] ips) throws Exception {
//        alter user zwtest hosts'10.20.64.29 10.20.64.30';
        String ipStr = StringUtils.join(ips, " ");
        String sql = "ALTER USER  " + identifier + " HOSTS '" + ipStr + "'";
        stmt = connection.createStatement();
        stmt.execute(sql);
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
        }
        return num;
    }

    @Override
    public String queryMinTime(String schema, String tableName, String timeColumnName) throws Exception {
        String minTime = "";
        String sql = "SELECT MIN(" + timeColumnName + ") FROM " + schema + "." + tableName;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                minTime = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
        }
        return minTime;
    }

    @Override
    public String queryMaxTime(String schema, String tableName, String timeColumnName) throws Exception {
        String maxTime = "";
        String sql = "SELECT MAX(" + timeColumnName + ") FROM " + schema + "." + tableName;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                maxTime = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("错误：" + e.getMessage());
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
        }
        return num;
    }
}
