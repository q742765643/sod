package com.piesat.dm.core.template;

import lombok.Data;

/**
 * Cassandra 模板
 *
 * @author cwh
 * @date 2020年 09月17日 12:41:55
 */
@Data
public class SqlTemplateComm {
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
    public String GRANT_ANY_TABLE = "GRANT ${grantStr} ANY TABLE IN SCHEMA ${schema} To ${userName} ";
    public String DROP_SCHEMA = "DROP SCHEMA ${schema} ";
    public String DROP_TABLE = "DROP TABLE ${schema}.${tableName}";
    public String ALTER_ADD_COLUMN = "ALTER TABLE ${schema}.${tableName} ADD COLUMN ${columnName} ${type}(${precision}) ${isNull} ${def}";
    public String ALTER_DROP_COLUMN = "ALTER TABLE ${schema}.${tableName} DROP COLUMN ${columnName}";
    public String ALTER_RENAME_COLUMN = "ALTER TABLE ${schema}.${tableName} RENAME COLUMN ${oldColumnName} TO ${columnName}";
    public String ALTER_COLUMN_ATTR = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} ${type}<#if length??> (${length}<#if precision??>,${precision}</#if>)</#if>";
    public String ALTER_COLUMN_SET_DEFAULT = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} SET DEFAULT ${def}";
    public String ALTER_COLUMN_DROP_DEFAULT = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} DROP DEFAULT";
    public String ALTER_COLUMN_SET_NOTNULL = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} SET NOT NULL";
    public String ALTER_COLUMN_DROP_NOTNULL = "ALTER TABLE ${schema}.${tableName} ALTER COLUMN ${columnName} DROP NOT NULL";
    public String QUERY_COLUMN = "SELECT TABLE_SCHEMA,TABLE_NAME,UPPER(COLUMN_NAME) COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '${tableName}' AND TABLE_SCHEMA = '${schema}'";
    public String QUERY_COLUMN_ATTR = "SELECT TABLE_SCHEMA,TABLE_NAME,UPPER(COLUMN_NAME) COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '${tableName}' AND TABLE_SCHEMA = '${schema}' AND COLUMN_NAME = 'columnName'";
    public String QUERY_INDEX = "SELECT * FROM INFORMATION_SCHEMA.INDEX WHERE TABLE_NAME = '${tableName}' AND SCHEMA_NAME = '${schema}'";
    public String QUERY_TABLES = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '${schema}'";
    public String QUERY_ALL_TABLES = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES";

}
