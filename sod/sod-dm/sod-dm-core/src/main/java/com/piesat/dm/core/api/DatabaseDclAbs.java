package com.piesat.dm.core.api;

import java.sql.*;

/**
 * 数据库管理抽象类
 *
 * @author cwh
 * @date 2020年 02月04日 15:48:24
 */
public abstract class DatabaseDclAbs implements DatabaseDcl{
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
}
