package com.piesat.schedule.sync.client.util;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.sync.client.constants.ComConstants;
import com.piesat.schedule.sync.client.constants.SqlConstants;
import com.piesat.schedule.sync.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.sync.client.datasource.DynamicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cwh
 * @date 2020年 11月11日 14:59:16
 */
public class SelectUtil {

    /**
     * 获取表的列名
     *
     * @param databasePid
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<String> getColumn(String databasePid, String tableName) throws SQLException {
        List<String> l = new ArrayList<>();
        Connection con = null;
        DataSourceContextHolder.setDataSource(databasePid);
        String sql = String.format(SqlConstants.GET_COLUMN_SQL, tableName);
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        try {
            con = dynamicDataSource.getConnection();
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery(sql);
            ResultSetMetaData metaData = r.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String name = metaData.getColumnName(i).toUpperCase();
                l.add(name);
            }
        } catch (SQLException e) {
           throw e;
        }finally {
            DataSourceContextHolder.clearDataSource();
            if (con!=null){
                con.close();
            }
        }
        return l;
    }


    public List<Map<String, Object>> query(String databasePid, String tableName, List<String> columns, List<String> where) throws SQLException {

        List<Map<String, Object>> list = new ArrayList<>();
        Connection con = null;
        DataSourceContextHolder.setDataSource(databasePid);
        String whereStr = where.stream().collect(Collectors.joining(ComConstants.AND));
        String column = columns.stream().collect(Collectors.joining(ComConstants.SEPARATOR));
        String sql = String.format(SqlConstants.QUERY_SQL, column, tableName, whereStr);
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);

        try {
            con = dynamicDataSource.getConnection();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(md.getColumnName(i).toUpperCase(), rs.getObject(i));
                }
                list.add(map);
            }

        } catch (SQLException e) {
            throw e;
        }finally {
            DataSourceContextHolder.clearDataSource();
            if (con != null) {
                con.close();
            }
        }


        return list;
    }
}
