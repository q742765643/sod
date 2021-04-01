package com.piesat.dm.core.template;

/**
 * Cassandra 模板
 *
 * @author cwh
 * @date 2020年 09月17日 12:41:55
 */
public class SqlTemplateComm {
    public final String CREATE_USER = "CREATE USER ${userName} IDENTIFIED BY '${password}'";
    public final String ALTER_USER_PWD = "ALTER USER ${userName} IDENTIFIED BY '${password}'";
    public final String ALTER_USER_WHITELIST = "ALTER USER ${userName} HOSTS '${whitelistStr}'";
    public final String DROP_USER = "DROP USER ${userName} ";
    public final String QUERY_USER = "SELECT * FROM DBA_USERS WHERE  UPPER(USER_NAME) = UPPER('${userName}')";
    public final String LOCK_USER = "ALTER USER ${userName} ACCOUNT LOCK ";
    public final String GRANT_TABLE = "GRANT ${grantStr} ON ${schema}.${tableName} To ${userName} ";
    public final String REVOKE_TABLE = "REVOKE ${grantStr} ON ${schema}.${tableName} FROM ${userName} ";
    public final String QUERY_SCHEMA = "SELECT * FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '${schema}'";
    public final String CREATE_SCHEMA = "CREATE SCHEMA ${schema} AUTHORIZATION ${userName} ";
    public final String GRANT_ANY_TABLE = "GRANT ${grantStr} ANY TABLE IN SCHEMA ${schema} To ${userName} ";
    public final String DROP_SCHEMA = "DROP SCHEMA ${schema} ";
    public final String DROP_TABLE = "DROP TABLE ${schema}.${tableName}";
    public final String ALTER_ADD_COLUMN = "ALTER TABLE ${schema}.${tableName} ADD COLUMN ${columnName} ${type}(${precision}) ${isNull} ${def}";
    public final String ALTER_DROP_COLUMN = "ALTER TABLE ${schema}.${tableName} DROP COLUMN ${columnName}";
    public final String ALTER_RENAME_COLUMN = "ALTER TABLE ${schema}.${tableName} RENAME COLUMN ${oldColumnName} TO ${columnName}";
    public final String ALTER_COLUMN_ATTR = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} ${type}<#if length??> (${length}<#if precision??>,${precision}</#if>)</#if>";
    public final String ALTER_COLUMN_SET_DEFAULT = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} SET DEFAULT ${def}";
    public final String ALTER_COLUMN_DROP_DEFAULT = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} DROP DEFAULT";
    public final String ALTER_COLUMN_SET_NOTNULL = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} SET NOT NULL";
    public final String ALTER_COLUMN_DROP_NOTNULL = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} DROP NOT NULL";
    public final String QUERY_COLUMN = "SELECT TABLE_SCHEMA,TABLE_NAME,UPPER(COLUMN_NAME) COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '${tableName}' AND TABLE_SCHEMA = '${schema}'";
    public final String QUERY_COLUMN_ATTR = "SELECT TABLE_SCHEMA,TABLE_NAME,UPPER(COLUMN_NAME) COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '${tableName}' AND TABLE_SCHEMA = '${schema}' AND COLUMN_NAME = 'columnName'";
    public final String QUERY_INDEX = "SELECT * FROM INFORMATION_SCHEMA.INDEX WHERE TABLE_NAME = '${tableName}' AND SCHEMA_NAME = '${schema}'";
    public final String QUERY_TABLES = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '${schema}'";
    public final String QUERY_ALL_TABLES = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES";

}
