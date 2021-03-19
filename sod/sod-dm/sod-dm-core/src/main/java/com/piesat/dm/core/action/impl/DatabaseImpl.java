package com.piesat.dm.core.action.impl;

import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.OwnException;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.core.action.impl.abs.BaseAbs;
import com.piesat.dm.core.datasource.CommDataSource;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月26日 17:29:46
 */
@Slf4j
public class DatabaseImpl extends BaseAbs {

    private Connection con;

    @Override
    public BaseAbs init(ConnectVo c, ResultT r) {
        try {
            con = CommDataSource.getConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            r.setErrorMessage(String.format(ConstantsMsg.MSG8, c.getPid()));
        }
        return this;
    }

    @Override
    public void exe(String sql, ResultT r) {
        Statement s;
        try {
            s = con.createStatement();
            s.execute(sql);
            s.close();
        } catch (Exception e) {
            log.error(OwnException.get(e));
            r.setErrorMessage(OwnException.get(e));
        }
    }

    @Override
    public Boolean exeQuery(String sql) {
        Boolean b = false;
        Statement s = null;
        ResultSet r = null;
        try {
            s = con.createStatement();
            r = s.executeQuery(sql);
            b = r.next() ? true : false;
            r.close();
            s.close();
        } catch (Exception e) {
            log.error(OwnException.get(e));
        } finally {
            try {
                closeCon(s, r);
            } catch (SQLException exc) {
                log.error(OwnException.get(exc));
            }
        }
        return b;
    }

    @Override
    public List<Map<String, Object>> exeQuery(String sql, ResultT r) {
        List<Map<String, Object>> list = new ArrayList<>();
        Statement s = null;
        ResultSet rs = null;
        if (con == null) {
            return list;
        }
        try {
            s = con.createStatement();
            rs = s.executeQuery(sql);
            List<String> names = new ArrayList<>();
            ResultSetMetaData m = rs.getMetaData();
            for (int i = 1; i <= m.getColumnCount(); i++) {
                String columnName = m.getColumnName(i);
                names.add(columnName);
            }
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < names.size(); i++) {
                    String e = names.get(i);
                    try {
                        Object obj = rs.getObject(e);
                        if (obj instanceof java.util.Date) {
                            map.put(e, DateUtils.getDateTimeStr((Date) obj));
                        } else if (obj instanceof Blob) {
                            map.put(e, this.getString(null, (Blob) obj));
                        } else if (obj instanceof Clob) {
                            map.put(e, this.getString((Clob) obj, null));
                        } else {
                            map.put(e, rs.getObject(e));
                        }

                    } catch (SQLException exc) {
                        log.error(OwnException.get(exc));
                    }
                }
                list.add(map);
            }
            rs.close();
            s.close();
        } catch (Exception e) {
            log.error(OwnException.get(e));
            r.setErrorMessage(ConstantsMsg.MSG13);
        } finally {
            try {
                closeCon(s, rs);
            } catch (SQLException exc) {
                log.error(OwnException.get(exc));
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> exeQueryOne(String sql, ResultT r) {
        Statement s = null;
        ResultSet rs = null;
        try {
            s = con.createStatement();
            rs = s.executeQuery(sql);
            List<String> names = new ArrayList<>();
            ResultSetMetaData m = rs.getMetaData();
            for (int i = 1; i <= m.getColumnCount(); i++) {
                String columnName = m.getColumnName(i);
                names.add(columnName);
            }
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < names.size(); i++) {
                    String e = names.get(i);
                    try {
                        map.put(e, rs.getObject(e));
                    } catch (SQLException exc) {
                        log.error(OwnException.get(exc));
                    }
                }
                return map;
            }
            rs.close();
            s.close();
        } catch (SQLException e) {
            log.error(OwnException.get(e));
            r.setErrorMessage(ConstantsMsg.MSG13);
        } finally {
            try {
                closeCon(s, rs);
            } catch (SQLException exc) {
                log.error(OwnException.get(exc));
            }
        }
        return null;
    }

    @Override
    public void close() {
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭
     *
     * @param s
     * @param r
     * @throws SQLException
     */
    public void closeCon(Statement s, ResultSet r) throws SQLException {
        if (r != null && !r.isClosed()) {
            r.close();
        }
        if (s != null && !s.isClosed()) {
            s.close();
        }
    }

    public String getString(Clob c, Blob b) throws SQLException, UnsupportedEncodingException {
        String s;
        if (c != null) {
            //clob 转 String
            s = c.getSubString(1, (int) c.length());
        } else if (b != null) {
            //blob 转 String
            s = new String(b.getBytes(1, (int) b.length()), "GBK");
        } else {
            s = "";
        }
        return s;
    }
}
