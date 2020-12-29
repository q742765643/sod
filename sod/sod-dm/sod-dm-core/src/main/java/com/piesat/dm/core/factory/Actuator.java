package com.piesat.dm.core.factory;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.piesat.common.utils.OwnException;
import com.piesat.dm.core.constants.Constants;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.parser.ManagerUser;
import com.piesat.dm.core.template.TemplateCassandra;
import com.piesat.dm.core.template.TemplateGbase;
import com.piesat.dm.core.template.TemplatePostgreSql;
import com.piesat.dm.core.template.TemplateXuGu;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cwh
 * @date 2020年 12月08日 19:22:45
 */
@Slf4j
public class Actuator {
    /**
     * 公用模板
     */
    public String CREATE_USER = "CREATE USER ${userName} IDENTIFIED BY '${password}'";
    public String ALTER_USER_PWD = "ALTER USER ${userName} IDENTIFIED BY '${password}'";
    public String ALTER_USER_WHITELIST = "ALTER USER ${userName} HOSTS '${whitelistStr}'";
    public String DROP_USER = "DROP USER ${userName} ";
    public String QUERY_USER = "SELECT * FROM DBA_USERS WHERE  UPPER(USER_NAME) = UPPER('${userName}')";
    public String LOCK_USER = "ALTER USER ${userName} ACCOUNT LOCK ";
    public String GRANT_TABLE = "GRANT ${grantStr} ON ${schema}.${tableName} To ${userName} ";
    public String REVOKE_TABLE = "REVOKE ${grantStr} ON ${schema}.${tableName} FROM ${userName} ";
    public String QUERY_SCHEMA = "SELECT * FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '${schema}'";
    public String CREATE_SCHEMA = "CREATE SCHEMA ${schema} AUTHORIZATION ${userName} ";
    public String GRANT_ANY_TABLE = "GRANT ${grantStr}  ANY TABLE IN SCHEMA ${schema} To ${userName} ";
    public String DROP_SCHEMA = "DROP SCHEMA ${schema} ";
    public String DROP_TABLE = "DROP TABLE ${schema}.${tableName}";
    public String ALTER_ADD_COLUMN = "ALTER TABLE ${schema}.${tableName} ADD COLUMN ${columnName} ${type}(${precision}) ${isNull} ${def}";
    public String ALTER_RENAME_COLUMN = "ALTER TABLE ${schema}.${tableName} RENAME COLUMN ${oldColumnName} TO ${columnName}";
    public String QUERY_COLUMN = "SELECT TABLE_SCHEMA,TABLE_NAME,UPPER(COLUMN_NAME) COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '${tableName}' AND TABLE_SCHEMA = '${schema}'";
    public String QUERY_INDEX = "SELECT * FROM INFORMATION_SCHEMA.INDEX WHERE TABLE_NAME = '${tableName}' AND SCHEMA_NAME = '${schema}'";
    public String QUERY_TABLES = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '${schema}'";

    public String[] sysUsers =  ManagerUser.sysUser.split(Constants.COMMA);

    public DatabaseTypesEnum databaseType;

    private Connection con;

    private Session session;

    public Actuator(DatabaseTypesEnum databaseType, Object con) {
        if (databaseType.equals(DatabaseTypesEnum.CASSANDRA) && con instanceof Session) {
            this.session = (Session) con;
        } else {
            this.con = (Connection) con;
        }
        this.databaseType = databaseType;
        if (DatabaseTypesEnum.XUGU.equals(databaseType)) {
            loadProp(TemplateXuGu.class);
        } else if (DatabaseTypesEnum.GBASE.equals(databaseType)) {
            loadProp(TemplateGbase.class);
        } else if (DatabaseTypesEnum.CASSANDRA.equals(databaseType)) {
            loadProp(TemplateCassandra.class);
        } else if (DatabaseTypesEnum.POSTGRESQL.equals(databaseType)) {
            loadProp(TemplatePostgreSql.class);
        }
    }

    public void loadProp(Class c) {
        Field[] fields = c.getDeclaredFields();
        Field[] _fields = FieldUtils.getAllFields(this.getClass());
        for (Field field : fields) {
            for (Field _field : _fields) {
                if (_field.getName().equals(field.getName())) {
                    try {
                        _field.set(this, field.get(c));
                    } catch (IllegalAccessException e) {
                        log.error(OwnException.get(e));
                    }
                }
            }
        }
    }

    /**
     * 执行sql
     *
     * @param sql
     * @param resultT
     */
    public void exe(String sql, ResultT resultT) {
        Statement s;
        try {
            s = con.createStatement();
            s.execute(sql);
            s.close();
        } catch (SQLException throwables) {
            log.error(OwnException.get(throwables));
            resultT.setErrorMessage(OwnException.get(throwables));
        }
    }

    /**
     * 查询sql  返回Boolean
     *
     * @param sql
     * @return
     */
    public Boolean exeQuery(String sql) {
        Boolean b = false;
        if (databaseType.equals(DatabaseTypesEnum.CASSANDRA)) {
            com.datastax.driver.core.ResultSet rs = session.execute(sql);
            Iterator<Row> it = rs.iterator();
            b = it.hasNext() ? true : false;
        } else {
            Statement s = null;
            ResultSet r = null;
            try {
                s = con.createStatement();
                r = s.executeQuery(sql);
                b = r.next() ? true : false;
                r.close();
                s.close();
            } catch (SQLException throwables) {
                log.error(OwnException.get(throwables));
            } finally {
                try {
                    closeCon(s, r);
                } catch (SQLException exc) {
                    log.error(OwnException.get(exc));
                }
            }
        }
        return b;
    }

    public Boolean cassandraUserIn(String sql, String userName) {
        com.datastax.driver.core.ResultSet rs = session.execute(sql);
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            if (userName.equalsIgnoreCase(row.getObject(0).toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查询sql  返回list
     *
     * @param sql
     * @param t
     * @return
     */
    public List<Map<String, Object>> exeQuery(String sql, ResultT t) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (databaseType.equals(DatabaseTypesEnum.CASSANDRA)) {
            com.datastax.driver.core.ResultSet rs = session.execute(sql);
            List<ColumnDefinitions.Definition> definitions = rs.getColumnDefinitions().asList();
            List<String> names = definitions.stream().map(ColumnDefinitions.Definition::getName).collect(Collectors.toList());
            for (Row r : rs) {
                Map<String, Object> rowData = new HashMap<>();
                names.forEach(e -> {
                    rowData.put(e, r.getObject(e));
                });
                list.add(rowData);
            }
        } else {
            Statement s = null;
            ResultSet r = null;
            try {
                s = con.createStatement();
                r = s.executeQuery(sql);
                List<String> names = new ArrayList<>();
                ResultSetMetaData m = r.getMetaData();
                for (int i = 1; i <= m.getColumnCount(); i++) {
                    String columnName = m.getColumnName(i);
                    names.add(columnName);
                }
                while (r.next()) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 0; i < names.size(); i++) {
                        String e = names.get(i);
                        try {
                            map.put(e, r.getObject(e));
                        } catch (SQLException exc) {
                            log.error(OwnException.get(exc));
                        }
                    }
                    list.add(map);
                }
                r.close();
                s.close();
            } catch (SQLException e) {
                log.error(OwnException.get(e));
                t.setErrorMessage("查询数据出错！");
            } finally {
                try {
                    closeCon(s, r);
                } catch (SQLException exc) {
                    log.error(OwnException.get(exc));
                }
            }
        }
        return list;
    }

    public Map<String, Object> exeQueryOne(String sql, ResultT t) {
        if (databaseType.equals(DatabaseTypesEnum.CASSANDRA)) {
            com.datastax.driver.core.ResultSet rs = session.execute(sql);
            List<ColumnDefinitions.Definition> definitions = rs.getColumnDefinitions().asList();
            List<String> names = definitions.stream().map(ColumnDefinitions.Definition::getName).collect(Collectors.toList());
            for (Row r : rs) {
                Map<String, Object> rowData = new HashMap<>();
                names.forEach(e -> {
                    rowData.put(e, r.getObject(e));
                });
                return rowData;
            }
        } else {
            Statement s = null;
            ResultSet r = null;
            try {
                s = con.createStatement();
                r = s.executeQuery(sql);
                List<String> names = new ArrayList<>();
                ResultSetMetaData m = r.getMetaData();
                for (int i = 1; i <= m.getColumnCount(); i++) {
                    String columnName = m.getColumnName(i);
                    names.add(columnName);
                }
                while (r.next()) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 0; i < names.size(); i++) {
                        String e = names.get(i);
                        try {
                            map.put(e, r.getObject(e));
                        } catch (SQLException exc) {
                            log.error(OwnException.get(exc));
                        }
                    }
                    return map;
                }
                r.close();
                s.close();
            } catch (SQLException e) {
                log.error(OwnException.get(e));
                t.setErrorMessage("查询数据出错！");
            } finally {
                try {
                    closeCon(s, r);
                } catch (SQLException exc) {
                    log.error(OwnException.get(exc));
                }
            }
        }
        return null;
    }

    /**
     * 关闭连接信息
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

    public void close(){
        if (con!=null){
            Connection c = (Connection) this.con;
            try {
                if (!c.isClosed()){
                    c.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


}
