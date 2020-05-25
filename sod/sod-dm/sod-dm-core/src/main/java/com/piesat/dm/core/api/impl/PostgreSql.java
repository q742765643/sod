package com.piesat.dm.core.api.impl;

import com.piesat.dm.core.api.DatabaseDclAbs;
import com.piesat.util.ResultT;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-22 13:27
 **/
public class PostgreSql extends DatabaseDclAbs {
    private final String driver = "org.postgresql.Driver";
    public PostgreSql(String url, String user, String password) throws Exception {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("PostgreSql数据库连接失败");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("PostgreSql数据驱动类缺失");
        }
    }

    @Override
    public int getUserNum(String user) throws Exception {
        return 0;
    }

    @Override
    public void addUser(String identifier, String password, String[] ips) throws Exception {

    }

    @Override
    public void deleteUser(String identifier, String ip) throws Exception {

    }

    @Override
    public void deleteUser(String identifier) throws Exception {

    }

    @Override
    public void addEnable(String identifier, String resource, List<String> ips, int type) throws Exception {

    }

    @Override
    public void deleteEnable(String identifier, String resource, List<String> ips, int type) throws Exception {

    }

    @Override
    public void addPermissions(Boolean select, String resource, String tableName, String identifier, String password, List<String> ips) throws Exception {

    }

    @Override
    public void deletePermissions(String[] permissions, String resource, String tableName, String identifier, String password, List<String> ips) throws Exception {

    }

    @Override
    public void createSchemas(String schemaName, String dataBaseUser, String password, boolean dataAuthor, boolean creatAuthor, boolean dropAuthor, List<String> ips) throws Exception {

    }

    @Override
    public void dropSchemas(String schemaName) throws Exception {

    }

    @Override
    public ResultT createTable(String sql, String tableName, Boolean deleteOld) throws Exception {
        return null;
    }

    @Override
    public ResultT existTable(String schema, String tableName) throws Exception {
        return null;
    }

    @Override
    public ResultT updateAccount(String dataBaseUser, String newPassword) throws Exception {
        return null;
    }

    @Override
    public ResultT queryAllTableName(String schema) throws Exception {
        return null;
    }

    @Override
    public ResultT queryAllColumnInfo(String schema, String tableName) throws Exception {
        return null;
    }

    @Override
    public ResultT queryAllIndexAndShardingInfo(String schema, String tableName) throws Exception {
        return null;
    }

    @Override
    public void bindIp(String identifier, String[] ips) throws Exception {

    }

    @Override
    public String queryRecordNum(String schema, String tableName) throws Exception {
        return null;
    }

    @Override
    public String queryMinTime(String schema, String tableName, String timeColumnName) throws Exception {
        return null;
    }

    @Override
    public String queryMaxTime(String schema, String tableName, String timeColumnName) throws Exception {
        return null;
    }

    @Override
    public String queryIncreCount(String schema, String tableName, String timeColumnName, String beginTime, String endTime) throws Exception {
        return null;
    }
}

