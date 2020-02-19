package com.piesat.dm.core.api.impl;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.model.Column;
import com.piesat.util.ResultT;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cassandra数据库管理
 *
 * @author cwh
 * @date 2020年 02月04日 16:11:43
 */
public class Cassandra implements DatabaseDcl {
    private Session instance = null;
    private Cluster cluster = null;
    private Lock lock = new ReentrantLock();

    public Cassandra() {
    }

    public Cassandra(String ip, int port, String username, String password, String keyspace) throws Exception {
        String[] ips = ip.split(",");
        if (null == instance) {
            try {
                lock.lock();
                if (null == instance) {
                    cluster = Cluster.builder()
                            .addContactPoints(ips).withPort(port)
                            .withCredentials(username, password)
                            .build();
                    if (StringUtils.isNotBlank(keyspace)) {
                        instance = cluster.connect(keyspace);
                    } else {
                        instance = cluster.connect();
                    }
                    // 也可以针对一个特定的keyspace获取一个session
                    // instance = cluster.connect("mycas");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Cassandra数据库连接失败，请检查！ERROR INFO:" + e.getMessage());
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void closeConnect() {
        cluster.close();
        instance.close();
        if (null == cluster) {
            try {
                lock.lock();
                if (null == cluster) {
                    cluster.close();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void autoCommit(Boolean flag) {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void addUser(String identifier, String password, String[] ips) throws Exception {
        identifier = identifier.toLowerCase();
        String cql = "LIST USERS;";
        ResultSet rs = instance.execute(cql);
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            if (identifier.equals(row.getObject(0))) {
                throw new Exception("用户已存在!");
            }
        }
        cql = "CREATE USER " + identifier + " WITH PASSWORD '" + password + "' NOSUPERUSER";
        instance.execute(cql);
    }

    @Override
    public void deleteUser(String identifier, String ip) {
        identifier = identifier.toLowerCase();
        String cql = "DROP USER IF EXISTS " + identifier;
        instance.execute(cql);
    }

    @Override
    public void addEnable(String identifier, String resource, List<String> ips, int type) {

    }

    @Override
    public void deleteEnable(String identifier, String resource, List<String> ips, int type) {

    }

    @Override
    public void addPermissions(Boolean select, String resource, String tableName, String identifier, String password, List<String> ips) {
        identifier = identifier.toLowerCase();
        String permission = select ? "SELECT" : "SELECT,UPDATE,INSERT,DELETE";
        for (int i = 0; i < ips.size(); i++) {
            String cql = "GRANT " + permission + " ON " + resource + "." + tableName + " To " + identifier;
            instance.execute(cql);
        }
    }

    @Override
    public void deletePermissions(String[] permissions, String resource, String tableName, String identifier, String password, List<String> ips) throws Exception {
        identifier = identifier.toLowerCase();
        String permission = ArrayUtils.toString(permissions, ",");
        try {
            for (int i = 0; i < ips.size(); i++) {
                String cql = "REVOKE " + permission + " ON " + resource + "." + tableName + " FROM " + identifier;
                instance.execute(cql);
            }
        } catch (Exception e) {
            throw new Exception("撤销Cassandra数据库授权失败！errInfo：" + e.getMessage());
        }
    }

    @Override
    public void createSchemas(String schemaName, String dataBaseUser, String password, boolean dataAuthor, boolean creatAuthor, boolean dropAuthor, List<String> ips) throws Exception {
        try {
            dataBaseUser = dataBaseUser.toLowerCase();
            String cql = "CREATE KEYSPACE IF NOT EXISTS " + schemaName + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}";
            instance.execute(cql);
            if (dataAuthor) {
                cql = "GRANT MODIFY ON KEYSPACE " + schemaName + " TO " + dataBaseUser;
                instance.execute(cql);
            }
            if (creatAuthor) {
                cql = "GRANT CREATE ON KEYSPACE " + schemaName + " TO " + dataBaseUser;
                instance.execute(cql);
                cql = "GRANT ALTER ON KEYSPACE " + schemaName + " TO " + dataBaseUser;
                instance.execute(cql);
            }
            if (dropAuthor) {
                cql = "GRANT DROP ON KEYSPACE " + schemaName + " TO " + dataBaseUser;
                instance.execute(cql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("检查Cassandra数据库账户是否存在，errInfo：" + e.getMessage());
        }
    }

    @Override
    public void dropSchemas(String schemaName) {
        String cql = "DROP KEYSPACE " + schemaName;
        instance.execute(cql);
    }

    @Override
    public ResultT createTable(String sql, String tableName, Boolean deleteOld) {
        try {
            String cql = "DROP TABLE IF EXISTS " + tableName;
            if (deleteOld) instance.execute(cql);
            instance.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success();
    }

    @Override
    public ResultT existTable(String schema, String tableName) {
        String cql = "SELECT * FROM " + tableName + " LIMIT 10";
        try {
            ResultSet rs = instance.execute(cql);
            Iterator<Row> it = rs.iterator();
            int no = 0;
            while (it.hasNext()) {
                it.next();
                no++;
            }
            return ResultT.success(no);
        } catch (Exception e) {
            if (e.getMessage().contains("unconfigured table")) {
                return ResultT.success(-1);
            } else {
                e.printStackTrace();
                return ResultT.failed(e.getMessage());
            }
        }
    }

    @Override
    public ResultT updateAccount(String dataBaseUser, String newPassword) {
        String cql = "ALTER USER " + dataBaseUser + " WITH PASSWORD '" + newPassword + "'";
        try {
            instance.execute(cql);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success();
    }


    @Override
    public ResultT queryData(String schema, String tableName, List<String> column, int row) throws Exception {
        String columns = String.join(",", column);
        String sql = "SELECT " + columns + " FROM " + schema + "." + tableName + " limit " + row;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSet rs = instance.execute(sql);
        for (Row r : rs) {
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= column.size(); i++) {
                rowData.put(column.get(i - 1), r.getObject(i));
            }
            list.add(rowData);
        }
        return ResultT.success(list);
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
    public ResultT updateColumn(String schema, String tableName, Column oldColumn, Column newColumn) {
        return null;
    }
}
