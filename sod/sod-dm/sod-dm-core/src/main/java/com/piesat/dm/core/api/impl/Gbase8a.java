package com.piesat.dm.core.api.impl;

import com.piesat.dm.core.api.DatabaseDclAbs;
import com.piesat.util.ResultT;
import org.apache.commons.lang.ArrayUtils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * 8a数据库管理
 *
 * @author cwh
 * @date 2020年 02月04日 16:11:06
 */
public class Gbase8a extends DatabaseDclAbs {
    private final String driver = "com.gbase.jdbc.Driver";

    public Gbase8a(String url, String user, String password) throws Exception{
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Gbase8a数据库连接失败");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Gbase8a数据驱动类缺失");
        }
    }


    public int getUserNum(String user) throws Exception {
        int num = 0;
        String sql = "SELECT COUNT(*) FROM GBASE.USER WHERE USER='" + user + "'";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();
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
            throw new Exception("数据库用户已经存在！");
        }
        for (String ip : ips) {
            String sql = "CREATE USER '" + identifier + "'@'" + ip + "' IDENTIFIED BY '" + password + "'";
            try {
                stmt = connection.createStatement();
                stmt.execute(sql);
            } catch (SQLException e) {
                throw new Exception("新增用户失败！errInfo：" + e.getMessage());
            }
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

    public void deleteUser(String identifier) throws Exception {
        String sql = "SELECT HOST FROM GBASE.USER WHERE USER ='" + identifier + "'";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String host = rs.getString("HOST").trim();
                sql = "DROP USER ?@?";
                ps = connection.prepareStatement(sql);
                ps.setString(1, identifier);
                ps.setString(2, host);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new Exception("删除用户失败！errInfo：" + e.getMessage());
        }
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
        for (int i = 0; i < ips.size(); i++) {
            String sql = "GRANT " + permission + " ON " + resource + "." + tableName + " To '" + identifier + "'@'" + ips.get(i) + "' IDENTIFIED BY '" + password + "'";
            System.out.println(sql);
            stmt.execute(sql);
        }
    }

    @Override
    public void deletePermissions(String[] permissions, String resource, String tableName, String identifier, String password, List<String> ips) throws Exception {
        String permission = ArrayUtils.toString(permissions, ",");
        try {
            stmt = connection.createStatement();
            for (int i = 0; i < ips.size(); i++) {
                String sql = "REVOKE " + permission + " ON " + resource + "." + tableName + " FROM '" + identifier + "'@'" + ips.get(i) + "'";
                System.out.println(sql);
                stmt.execute(sql);
            }
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
            for (int i = 0; i < ips.size(); i++) {
                for (int j = 0; j < permissions.length; j++) {
                    sql = "GRANT " + permissions[j] + " ON " + schemaName + ".* to '" + dataBaseUser + "'@'" + ips.get(i) + "' IDENTIFIED BY '" + password + "'";
                    if ((dataAuthor && j == 0) || (creatAuthor && j == 1) || (dropAuthor && j == 2)) {
                        stmt.execute(sql);
                    }
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
            return ResultT.success(num);
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("doesn't exist")) {
                return ResultT.success(-1);
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
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success();
    }
}
