package com.piesat.dm.core.api;

import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.model.Column;
import com.piesat.util.ResultT;

import java.sql.*;
import java.util.*;

/**
 * 数据库管理抽象类
 *
 * @author cwh
 * @date 2020年 02月04日 15:48:24
 */
public abstract class AbstractDatabaseDcl implements DatabaseDcl {

    public static final String USER_NAME = "userName";
    public static final String PASSWORD = "password";
    public static final String TABLE_NAME = "tableName";
    public static final String SELECT_GRANT = "SELECT";
    public static final String ALL_GRANT = "SELECT,UPDATE,INSERT,DELETE";

    public Connection connection;
    public Statement stmt = null;
    public PreparedStatement ps = null;
    public ResultSet rs = null;

    @Override
    public void closeConnect() {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void autoCommit(Boolean flag) {
        try {
            connection.setAutoCommit(flag);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultT queryData(String schema,String tableName, List<String> column,int row) throws Exception {
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

    @Override
    public ResultT updateColumn(String schema, String tableName, Column oldColumn, Column newColumn){
        List<String> sqlList = new ArrayList<>();
        String name = schema + "." + tableName;
        if (oldColumn == null){
//            alter table test1 add column name111 varchar(101) not null DEFAULT '1111'
            String isNull = newColumn.getIsNull() ? "" : "NOT NULL";
            String def =  StringUtils.isEmpty(newColumn.getDef()) ? "" : "DEFAULT '"+newColumn.getDef()+"'";
            String format = "alter table %s add column %s %s(%s) %s %s";
            String sql = String.format(format, name, newColumn.getName(), newColumn.getType(), newColumn.getPrecision(), isNull, def);
            sqlList.add(sql);
        }else {
            if (!oldColumn.getName().equals(newColumn.getName())){
//                alter table test1 rename column name111 to bbbbb
                String format = "alter table %s rename column %s to %s";
                String.format(format,name,oldColumn.getName(),newColumn.getName());
            }

        }

        return null;
    }
}
