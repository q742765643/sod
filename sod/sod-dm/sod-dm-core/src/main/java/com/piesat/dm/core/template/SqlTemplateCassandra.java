package com.piesat.dm.core.template;

import lombok.Data;

/**
 * Cassandra 模板
 *
 * @author cwh
 * @date 2020年 09月17日 12:41:55
 */
@Data
public class SqlTemplateCassandra extends SqlTemplateComm {
    public String QUERY_SCHEMA = "SELECT * FROM DBA_SCHEMAS WHERE SCHEMA_NAME = '${schema}'";
    public String QUERY_USER = "LIST USERS";
    public String QUERY_COLUMN = "SELECT KEYSPACE_NAME AS TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME AS COLUMN_NAME,TYPE AS COLUMN_TYPE FROM SYSTEM_SCHEMA.COLUMNS WHERE KEYSPACE_NAME = '${schema}' AND TABLE_NAME = '${tableName}' ";
    public String QUERY_INDEX = "SELECT KEYSPACE_NAME AS TABLE_SCHEMA,TABLE_NAME,INDEX_NAME,OPTIONS AS INDEX_COLUMN FROM SYSTEM_SCHEMA.INDEXES WHERE KEYSPACE_NAME = '${schema}' AND TABLE_NAME = '${tableName}' ";
    public String CREATE_SCHEMA = "CREATE KEYSPACE IF NOT EXISTS ${schema} WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 4}";
    public String GRANT_ANY_TABLE = "GRANT CREATE ON KEYSPACE ${grantStr} TO '${userName}' ";
    public String QUERY_TABLES = "SELECT TABLE_NAME FROM SYSTEM_SCHEMA.TABLES WHERE KEYSPACE_NAME = '${schema}'";
    public String ALTER_ADD_COLUMN = "ALTER TABLE ${schema}.${tableName} ADD ${columnName} ${type}";
    public String ALTER_DROP_COLUMN = "ALTER TABLE ${schema}.${tableName} DROP ${columnName}";
    public String ALTER_RENAME_COLUMN = "";
    public String ALTER_COLUMN_ATTR = "";
    public String ALTER_COLUMN_SET_DEFAULT = "";
    public String ALTER_COLUMN_DROP_DEFAULT = "";
    public String ALTER_COLUMN_SET_NOTNULL = "";
    public String ALTER_COLUMN_DROP_NOTNULL = "";
    public String ALTER_USER_WHITELIST = "";
    public String QUERY_ALL_TABLES = "SELECT TABLE_NAME FROM SYSTEM_SCHEMA.TABLES";
}
