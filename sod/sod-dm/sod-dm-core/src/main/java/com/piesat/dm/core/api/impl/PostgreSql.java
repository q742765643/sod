package com.piesat.dm.core.api.impl;

import com.piesat.dm.core.api.AbstractDatabaseDcl;
import com.piesat.util.ResultT;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-22 13:27
 **/
public class PostgreSql extends AbstractDatabaseDcl {
    private static final String DRIVER = "org.postgresql.Driver";

    public PostgreSql(String url, String user, String password) throws Exception {
        try {
            Class.forName(DRIVER);
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
        int num = 0;
        String sql = "SELECT COUNT(*) from pg_user where username='" + user + "'";
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
    public void addUser(String identifier, String password, String[] ips) throws Exception {
        int userNum = getUserNum(identifier);
        if (userNum > 0) {
            throw new Exception("数据库用户已存在！");
        }
        String sql="create user "+identifier+" with password '"+password+"'";
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new Exception("新增用户失败！errInfo：" + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String identifier, String ip) throws Exception {
        deleteUser(identifier);
    }

    @Override
    public void deleteUser(String identifier) throws Exception {
        int userNum = getUserNum(identifier);
        try {
            if (userNum > 0) {
                String sql = "DROP USER " + identifier;
                stmt = connection.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("删除用户失败！errInfo：" + e.getMessage());
        }
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
        String sql = "SELECT COUNT(*) FROM information_schema.schemata  WHERE SCHEMA_NAME = '" + schemaName + "'";
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
                    sql = "GRANT " + as[i] + " ON ALL TABLES IN SCHEMA " + schemaName + " TO " + dataBaseUser;
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
            }
            if (creatAuthor) {
                //建表权限
                stmt = connection.createStatement();
                String[] as = {"CREATE"};
                for (int i = 0; i < as.length; i++) {
                    sql = "GRANT " + as[i] + " ON ALL TABLES IN SCHEMA " + schemaName + " TO " + dataBaseUser;
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
            }
            if (dropAuthor) {
                //删表权限
                stmt = connection.createStatement();
                String[] as = {"TRUNCATE"};
                for (int i = 0; i < as.length; i++) {
                    sql = "GRANT " + as[i] + " ON ALL TABLES IN SCHEMA " + schemaName + " TO " + dataBaseUser;
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

    }

    @Override
    public ResultT createTable(String sql, String tableName, Boolean deleteOld) throws Exception {
        try {
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
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
    public ResultT existTable(String schema, String tableName) throws Exception {
        if(schema.equals(schema.toUpperCase())){
            schema="\""+schema+"\"";
        }
        String sql = "SELECT * FROM " + schema + "." + tableName + " LIMIT 10";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            int num = 0;
            while (rs.next()) {
                num++;
            }
            if (num > 0) return ResultT.success(true);
            else return ResultT.success(false);
        } catch (SQLException e) {
            if (e.getMessage().contains("not exist")||e.getMessage().contains("不存在")) {
                return ResultT.success(false);
            } else {
                e.printStackTrace();
                return ResultT.failed(e.getMessage());
            }

        }
    }

    @Override
    public ResultT updateAccount(String dataBaseUser, String newPassword) throws Exception {
        return null;
    }

    @Override
    public ResultT queryAllTableName(String schema) throws Exception {
        if(schema.equals(schema.toUpperCase())){
           // schema="\""+schema+"\"";
        }
        schema=schema.replaceAll("\"","");
        List<String> list = new ArrayList<String>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getTables(null, schema, null, new String[]{"TABLE"});
            while (rs.next()) {
                    list.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success(list);
    }

    @Override
    public ResultT queryAllColumnInfo(String schema, String tableName) throws Exception {
        if(schema.equals(schema.toUpperCase())){
            //schema="\""+schema+"\"";
        }
        schema=schema.replaceAll("\"","");
        Map<String, Map<String, Object>> columnInfos = new HashMap<String, Map<String, Object>>();
        String sql = "select  table_schema,table_name,upper(column_name) column_name,udt_name column_type,is_nullable" +
                " from information_schema.columns where TABLE_NAME= '" + tableName + "' and table_schema='" + schema + "'";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map columnOneInfo = new HashMap<String, Object>();
                columnOneInfo.put("column_name", rs.getString("column_name"));
                columnOneInfo.put("column_type", rs.getString("column_type"));//类型和精度 decimal(4,0)  varchar(200)
                columnOneInfo.put("column_comment", "");//字段含义
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
        schema=schema.replaceAll("\"","");
        HashMap<String, String> indexs = new HashMap<String, String>();
        HashMap<String, String> shardings = new HashMap<String, String>();
        Map<String, Map<String, String>> results = new HashMap<String, Map<String, String>>();
        String sql = "select\n" +
                "A.SCHEMANAME,\n" +
                "A.TABLENAME,\n" +
                "A.INDEXNAME indexname,\n" +
                "A.INDEXDEF indexdef,\n" +
                "A.TABLESPACE,\n" +
                "A.INDEXDEF,\n" +
                "B.AMNAME,\n" +
                "C.INDEXRELID,\n" +
                "C.INDNATTS,\n" +
                "C.INDISUNIQUE,\n" +
                "C.INDISPRIMARY,\n" +
                "C.INDISCLUSTERED,\n" +
                "D.DESCRIPTION\n" +
                "from\n" +
                "PG_AM B left join PG_CLASS F on\n" +
                "B.OID = F.RELAM left join PG_STAT_ALL_INDEXES E on\n" +
                "F.OID = E.INDEXRELID left join PG_INDEX C on\n" +
                "E.INDEXRELID = C.INDEXRELID left outer join PG_DESCRIPTION D on\n" +
                "C.INDEXRELID = D.OBJOID,\n" +
                "PG_INDEXES A\n" +
                "where\n" +
                "A.SCHEMANAME = E.SCHEMANAME\n" +
                "and A.TABLENAME = E.RELNAME\n" +
                "and A.INDEXNAME = E.INDEXRELNAME\n" +
                "and E.SCHEMANAME = '"+schema+"'\n" +
                "and E.RELNAME = '"+tableName+"'";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                //if (rs.getString("indexdef").toUpperCase().indexOf("UNIQUE")!=-1) {
                    //shardings.put(rs.getString("indexname"), rs.getString("indexname"));
                //} else {
                    indexs.put(rs.getString("indexname"), rs.getString("indexname"));
                //}
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

    }

    @Override
    public String queryRecordNum(String schema, String tableName) throws Exception {
        if(schema.equals(schema.toUpperCase())){
            schema="\""+schema+"\"";
        }
        String num = "";
        String sql = "SELECT COUNT(*) as COUNT FROM "+schema+"."+tableName;
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
        return  num;
    }

    @Override
    public String queryMinTime(String schema, String tableName, Date newBoundBeginTime, String timeColumnName) throws Exception {
        if(schema.equals(schema.toUpperCase())){
            schema="\""+schema+"\"";
        }
        String minTime = "";
        if(newBoundBeginTime != null){
            minTime = String.valueOf(newBoundBeginTime.getTime());
        }else {

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
        }
        return minTime;
    }

    @Override
    public String queryMaxTime(String schema, String tableName, Date newBoundEndTime,String newBoundEndTimeFlag, String timeColumnName) throws Exception {
        if(schema.equals(schema.toUpperCase())){
            schema="\""+schema+"\"";
        }
        String maxTime = "";
        String sql = "SELECT MAX(" + timeColumnName + ") FROM " + schema + "." + tableName;
        if(newBoundEndTime != null){
            maxTime = String.valueOf(newBoundEndTime.getTime());
        }else if(newBoundEndTimeFlag !=null || "".equalsIgnoreCase(newBoundEndTimeFlag)){
            int num = Integer.parseInt(newBoundEndTimeFlag);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + num);
            maxTime = String.valueOf(calendar.getTimeInMillis());
        }else {
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
        }
        return maxTime;
    }

    @Override
    public String queryIncreCount(String schema, String tableName, String timeColumnName, String beginTime, String endTime) throws Exception {
        if(schema.equals(schema.toUpperCase())){
            schema="\""+schema+"\"";
        }
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
    @Override
    public ResultT queryData(String schema,String tableName, List<String> column,int row) throws Exception {
        if(schema.equals(schema.toUpperCase())){
            schema="\""+schema+"\"";
        }
        String columns = String.join(",", column);
        String sql = "SELECT " + columns + " FROM "+schema+"."+tableName+" limit "+row;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String,Object> rowData = new HashMap<String,Object>();
                for (int i = 1; i <= column.size(); i++) {
                    rowData.put(column.get(i-1), rs.getObject(i)+"");
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
//            throw new Exception("数据查询异常：请在对应物理库内创建表结构"+tableName);
        }
        return ResultT.success(list);
    }

}

