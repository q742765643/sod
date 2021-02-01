package com.piesat.dm.core.action.impl;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.core.action.BaseAction;
import com.piesat.dm.core.action.impl.abs.BaseAbs;
import com.piesat.dm.core.datasource.CassandraSource;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.util.ResultT;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月26日 17:29:46
 */
public class CassandraDatabaseImpl extends BaseAbs {

    private Session con;

    @Override
    public BaseAbs init(ConnectVo c, ResultT r) {
        lower = false;
        try {
            con = CassandraSource.getConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                con.close();
            }
            r.setErrorMessage(String.format(ConstantsMsg.MSG8, c.getPid()));
        }
        return this;
    }

    @Override
    public void exe(String sql, ResultT r) {
        try {
            con.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
            r.setErrorMessage(ConstantsMsg.MSG17);
        }
    }

    @Override
    public Boolean exeQuery(String sql) {
        ResultSet rs = con.execute(sql);
        Iterator<Row> it = rs.iterator();
        return it.hasNext() ? true : false;
    }

    @Override
    public List<Map<String, Object>> exeQuery(String sql, ResultT r) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (con == null) {
            return list;
        }
        ResultSet rs = con.execute(sql);
        List<ColumnDefinitions.Definition> definitions = rs.getColumnDefinitions().asList();
        List<String> names = definitions.stream().map(ColumnDefinitions.Definition::getName).collect(Collectors.toList());
        for (Row row : rs) {
            Map<String, Object> rowData = new HashMap<>();
            names.forEach(e -> {
                rowData.put(e, row.getObject(e));
            });
            list.add(rowData);
        }
        return list;
    }

    @Override
    public Map<String, Object> exeQueryOne(String sql, ResultT r) {
        ResultSet rs = con.execute(sql);
        List<ColumnDefinitions.Definition> definitions = rs.getColumnDefinitions().asList();
        List<String> names = definitions.stream().map(ColumnDefinitions.Definition::getName).collect(Collectors.toList());
        for (Row row : rs) {
            Map<String, Object> rowData = new HashMap<>();
            names.forEach(e -> {
                rowData.put(e, row.getObject(e));
            });
            return rowData;
        }
        return null;
    }

    public Boolean cassandraUserIn(String sql, String userName) {
        ResultSet rs = con.execute(sql);
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            if (userName.equalsIgnoreCase(row.getObject(0).toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void close() {
    }
}
