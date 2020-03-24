//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elasticsearch.xpack.sql.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

class JdbcConnection implements Connection, JdbcWrapper {
    private final String url;
    private final String userName;
    final JdbcConfiguration cfg;
    final JdbcHttpClient client;
    private boolean closed;
    private String catalog;
    private String schema;

    JdbcConnection(JdbcConfiguration connectionInfo) throws SQLException {
        this(connectionInfo, true);
    }

    JdbcConnection(JdbcConfiguration connectionInfo, boolean checkServer) throws SQLException {
        this.closed = false;
        this.cfg = connectionInfo;
        this.client = new JdbcHttpClient(connectionInfo, checkServer);
        this.url = connectionInfo.connectionString();
        this.userName = connectionInfo.authUser();
    }

    private void checkOpen() throws SQLException {
        if(this.isClosed()) {
            throw new SQLException("Connection is closed");
        }
    }

    public Statement createStatement() throws SQLException {
        this.checkOpen();
        return new JdbcStatement(this, this.cfg);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        this.checkOpen();
        return new JdbcPreparedStatement(this, this.cfg, sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("Stored procedures not supported yet");
    }

    public String nativeSQL(String sql) throws SQLException {
        this.checkOpen();
        return sql;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.checkOpen();
        if(!autoCommit) {
            new SQLFeatureNotSupportedException("Non auto-commit is not supported");
        }

    }

    public boolean getAutoCommit() throws SQLException {
        this.checkOpen();
        return true;
    }

    public void commit() throws SQLException {
        this.checkOpen();
        if(this.getAutoCommit()) {
            throw new SQLException("Auto-commit is enabled");
        } else {
            throw new SQLFeatureNotSupportedException("Commit/Rollback not supported");
        }
    }

    public void rollback() throws SQLException {
        this.checkOpen();
        if(this.getAutoCommit()) {
            throw new SQLException("Auto-commit is enabled");
        } else {
            throw new SQLFeatureNotSupportedException("Commit/Rollback not supported");
        }
    }

    public void close() throws SQLException {
        if(!this.isClosed()) {
            this.closed = true;
            Debug.release(this.cfg);
        }

    }

    public boolean isClosed() {
        return this.closed;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return new JdbcDatabaseMetaData(this);
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        readOnly=true;
        if(!readOnly) {
            throw new SQLFeatureNotSupportedException("Only read-only mode is supported");
        }
    }

    public boolean isReadOnly() throws SQLException {
        this.checkOpen();
        return true;
    }

    public void setCatalog(String catalog) throws SQLException {
        this.checkOpen();
        this.catalog = catalog;
    }

    public String getCatalog() throws SQLException {
        this.checkOpen();
        return this.catalog;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        this.checkOpen();
        if(0 != level) {
            throw new SQLFeatureNotSupportedException("Transactions not supported");
        }
    }

    public int getTransactionIsolation() throws SQLException {
        this.checkOpen();
        return 0;
    }

    public SQLWarning getWarnings() throws SQLException {
        this.checkOpen();
        return null;
    }

    public void clearWarnings() throws SQLException {
        this.checkOpen();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        this.checkResultSet(resultSetType, resultSetConcurrency);
        return this.createStatement();
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        this.checkResultSet(resultSetType, resultSetConcurrency);
        return this.prepareStatement(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        this.checkResultSet(resultSetType, resultSetConcurrency);
        return this.prepareCall(sql);
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("typeMap not supported");
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("typeMap not supported");
    }

    public void setHoldability(int holdability) throws SQLException {
        this.checkOpen();
        this.checkHoldability(holdability);
    }

    public int getHoldability() throws SQLException {
        this.checkOpen();
        return 1;
    }

    public Savepoint setSavepoint() throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Savepoints not supported");
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Savepoints not supported");
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Savepoints not supported");
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Savepoints not supported");
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        this.checkOpen();
        this.checkHoldability(resultSetHoldability);
        return this.createStatement(resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        this.checkOpen();
        this.checkHoldability(resultSetHoldability);
        return this.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        this.checkOpen();
        this.checkHoldability(resultSetHoldability);
        return this.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        this.checkOpen();
        if(autoGeneratedKeys != 2) {
            throw new SQLFeatureNotSupportedException("Auto generated keys must be NO_GENERATED_KEYS");
        } else {
            return this.prepareStatement(sql);
        }
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Autogenerated key not supported");
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Autogenerated key not supported");
    }

    public Clob createClob() throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Clob not supported yet");
    }

    public Blob createBlob() throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Blob not supported yet");
    }

    public NClob createNClob() throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("NClob not supported yet");
    }

    public SQLXML createSQLXML() throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("SQLXML not supported yet");
    }

    public boolean isValid(int timeout) throws SQLException {
        if(timeout < 0) {
            throw new SQLException("Negative timeout");
        } else {
            return !this.isClosed() && this.client.ping(TimeUnit.SECONDS.toMillis((long)timeout));
        }
    }

    private void checkOpenClientInfo() throws SQLClientInfoException {
        if(this.isClosed()) {
            throw new SQLClientInfoException("Connection closed", (Map)null);
        }
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        this.checkOpenClientInfo();
        throw new SQLClientInfoException("Unsupported operation", (Map)null);
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this.checkOpenClientInfo();
        throw new SQLClientInfoException("Unsupported operation", (Map)null);
    }

    public String getClientInfo(String name) throws SQLException {
        this.checkOpenClientInfo();
        return null;
    }

    public Properties getClientInfo() throws SQLException {
        this.checkOpenClientInfo();
        return new Properties();
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Array not supported yet");
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("Struct not supported yet");
    }

    public void setSchema(String schema) throws SQLException {
        this.checkOpen();
        this.schema = schema;
    }

    public String getSchema() throws SQLException {
        this.checkOpen();
        return this.schema;
    }

    public void abort(Executor executor) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int getNetworkTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    private void checkResultSet(int resultSetType, int resultSetConcurrency) throws SQLException {
        if(1003 != resultSetType) {
            throw new SQLFeatureNotSupportedException("ResultSet type can only be TYPE_FORWARD_ONLY");
        } else if(1007 != resultSetConcurrency) {
            throw new SQLFeatureNotSupportedException("ResultSet concurrency can only be CONCUR_READ_ONLY");
        }
    }

    private void checkHoldability(int resultSetHoldability) throws SQLException {
        if(1 != resultSetHoldability) {
            throw new SQLFeatureNotSupportedException("Holdability can only be HOLD_CURSORS_OVER_COMMIT");
        }
    }

    String getURL() {
        return this.url;
    }

    String getUserName() {
        return this.userName;
    }

    int esInfoMajorVersion() throws SQLException {
        return this.client.serverInfo().majorVersion;
    }

    int esInfoMinorVersion() throws SQLException {
        return this.client.serverInfo().minorVersion;
    }
}
