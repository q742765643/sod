package com.piesat.dm.core.template;

/**
 * Cassandra 模板
 *
 * @author cwh
 * @date 2020年 09月17日 12:41:55
 */
public class TemplateCassandra {
    public static final String QUERY_SCHEMA = "SELECT * FROM DBA_SCHEMAS WHERE SCHEMA_NAME = '${schema}'";
    public static final String QUERY_USER = "LIST USERS";
    public static final String QUERY_COLUMN = "SELECT KEYSPACE_NAME AS TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME AS COLUMN_NAME,TYPE AS COLUMN_TYPE FROM SYSTEM_SCHEMA.COLUMNS WHERE KEYSPACE_NAME = '${schema}' AND TABLE_NAME = '${tableName}' ";
    public static final String QUERY_INDEX = "SELECT KEYSPACE_NAME AS TABLE_SCHEMA,TABLE_NAME,INDEX_NAME,OPTIONS AS INDEX_COLUMN FROM SYSTEM_SCHEMA.INDEXES WHERE KEYSPACE_NAME = '${schema}' AND TABLE_NAME = '${tableName}' ";
    public static final String CREATE_SCHEMA = "CREATE KEYSPACE IF NOT EXISTS ${schema} WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 4}";
    public static final String GRANT_ANY_TABLE = "GRANT CREATE ON KEYSPACE ${grantStr} TO '${userName}' ";

}
