package com.piesat.dm.core.api;

import com.piesat.dm.core.model.Column;
import com.piesat.util.ResultT;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理抽象类
 *
 * @author cwh
 * @date 2020年 02月04日 15:48:24
 */
public abstract class DatabaseDclAbs implements DatabaseDcl {
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
                    rowData.put(column.get(i-1), rs.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            throw new Exception("错误：" + e.getMessage());
        }
        return ResultT.success(list);
    }

    @Override
    public ResultT updateColumn(String schema, String tableName, Column oldColumn, Column newColumn){
        List<String> sqlList = new ArrayList<>();
        if (oldColumn == null){
            String sql = "alter table test1 add column name111 varchar(101) not null DEFAULT '1111'";
        }
        return null;
    }
}
