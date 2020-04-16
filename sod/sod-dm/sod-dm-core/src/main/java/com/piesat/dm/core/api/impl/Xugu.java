package com.piesat.dm.core.api.impl;

import com.piesat.dm.core.api.DatabaseDclAbs;
import com.piesat.util.ResultT;
import org.apache.commons.lang.ArrayUtils;

import javax.xml.transform.Result;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 虚谷数据库管理
 *
 * @author cwh
 * @date 2020年 02月04日 15:50:07
 */
public class Xugu extends DatabaseDclAbs {
    private final String driver = "com.xugu.cloudjdbc.Driver";

    public Xugu(String url, String user, String password) throws Exception {
        try {
            Class.forName(driver);
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
            throw new Exception("数据库用户已经存在！");
        }
        String sql = "CREATE USER " + identifier + " IDENTIFIED BY '" + password + "'";
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("新增用户失败！errInfo：" + e.getMessage());
        }

    }

    public int getUserNum(String user) throws Exception {
        int num = 0;
        String sql = "SELECT COUNT(*) FROM DBA_USERS WHERE USER_NAME='" + user.toUpperCase() + "'";
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
        int num = 0;
        String sql = "SELECT COUNT(*) FROM DBA_USERS WHERE USER_NAME='" + identifier.toUpperCase() + "'";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                num = rs.getInt(1);
            }
            if (num > 0) {
                sql = "DROP USER " + identifier;
                stmt = connection.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("删除用户失败！errInfo：" + e.getMessage());
        }
    }

    public void disabledUser(String identifier) throws Exception {
        String sql = "ALTER USER " + identifier + " ACCOUNT LOCK ";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
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
        String permission = select ? "SELECT" : "SELECT,UPDATE,INSERT,DELETE";
        String sql = "GRANT " + permission + " ON " + resource + "." + tableName + " To " + identifier;
        stmt = connection.createStatement();
        rs = stmt.executeQuery(sql);
    }

    @Override
    public void deletePermissions(String[] permissions, String resource, String tableName, String identifier, String password, List<String> ips) throws Exception {
        String permission = ArrayUtils.toString(permissions, ",");
        String sql = "REVOKE " + permission + " ON " + resource + "." + tableName + " FROM " + identifier;
        try {
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
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
            if (num == 0) {
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
                ps = connection.prepareStatement(sql);
                ps.executeUpdate();
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
            if (num>0)return ResultT.success(true);
            else return ResultT.success(false);
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
            stmt.execute(delSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success();
    }

    @Override
    public ResultT queryAllTableName(String schema) throws Exception {
        List<String> list = new ArrayList<String>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getTables(null, schema, null,new String[] { "TABLE" });
            while (rs.next()) {
                if(!rs.getString("TABLE_NAME").contains("#")){
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
        Map<String,Map<String,Object>> columnInfos = new HashMap<String,Map<String,Object>>();
        String sql = "select  table_schema,table_name,upper(column_name) column_name,column_type,is_nullable,column_comment"+
                " from information_schema.columns where TABLE_NAME= '"+tableName+"' and table_schema='"+schema+"'";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map columnOneInfo = new HashMap<String,Object>();
                columnOneInfo.put("column_name", rs.getString("column_name"));
                columnOneInfo.put("column_type", rs.getString("column_type"));//类型和精度 decimal(4,0)  varchar(200)
                columnOneInfo.put("column_comment", rs.getString("column_comment"));//字段含义
                if("NO".equals(rs.getString("is_nullable"))){
                    columnOneInfo.put("is_nullable", 0);
                }else{
                    columnOneInfo.put("is_nullable", 1);
                }
                columnInfos.put(rs.getString("column_name"),columnOneInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success(columnInfos);
    }

    @Override
    public ResultT queryAllIndexAndShardingInfo(String schema, String tableName) throws Exception {
        HashMap<String,String> indexs = new HashMap<String,String>();
        HashMap<String,String> shardings = new HashMap<String,String>();
        Map<String,Map<String,String>> results = new HashMap<String,Map<String,String>>();
        String sql = "select * from information_schema.index where table_name='"+tableName+"' and schema_name='"+schema+"'";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                if(rs.getString("index_name").startsWith("auto")){
                    shardings.put(rs.getString("index_name"), rs.getString("index_column"));
                }else{
                    indexs.put(rs.getString("index_name"), rs.getString("index_column"));
                }
            }
            results.put("indexs",indexs);
            results.put("shardings",shardings);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success(results);
    }

}
