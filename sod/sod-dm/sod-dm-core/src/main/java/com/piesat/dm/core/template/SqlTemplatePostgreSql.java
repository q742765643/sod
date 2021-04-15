package com.piesat.dm.core.template;

import lombok.Data;

/**
 * PostgreSql 模板
 *
 * @author cwh
 * @date 2020年 09月17日 12:41:55
 */
@Data
public class SqlTemplatePostgreSql extends SqlTemplateComm {

    public String CREATE_USER = "CREATE USER ${userName} WITH PASSWORD '${password}'";

    public String QUERY_USER = "SELECT * FROM PG_USER WHERE  UPPER(USERNAME) = UPPER('${userName}')";

    public String GRANT_ANY_TABLE = "GRANT ${grantStr} ON ALL TABLES IN SCHEMA ${schema} To ${userName} ";

    public String QUERY_TABLES = "SELECT TABLENAME AS TABLE_NAME FROM PG_TABLES WHERE SCHEMANAME = '${schema}'";

    public String QUERY_COLUMN =
            "SELECT TABLE_SCHEMA,TABLE_NAME,UPPER(COLUMN_NAME) COLUMN_NAME,UDT_NAME COLUMN_TYPE,IS_NULLABLE FROM " +
            "INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME= '${tableName}' AND TABLE_SCHEMA = '${schema}'";
    public String QUERY_INDEX =
            "SELECT A.SCHEMANAME, A.TABLENAME, A.INDEXNAME INDEXNAME, A.INDEXDEF INDEXDEF, A.TABLESPACE, A.INDEXDEF, B.AMNAME, " +
            "C.INDEXRELID, C.INDNATTS, C.INDISUNIQUE, C.INDISPRIMARY, C.INDISCLUSTERED, D.DESCRIPTION " +
            "FROM PG_AM B LEFT JOIN PG_CLASS F ON B.OID = F.RELAM " +
            "LEFT JOIN PG_STAT_ALL_INDEXES E ON F.OID = E.INDEXRELID " +
            "LEFT JOIN PG_INDEX C ON E.INDEXRELID = C.INDEXRELID " +
            "LEFT OUTER JOIN PG_DESCRIPTION D ON C.INDEXRELID = D.OBJOID, PG_INDEXES A " +
            "WHERE A.SCHEMANAME = E.SCHEMANAME AND A.TABLENAME = E.RELNAME and A.INDEXNAME = E.INDEXRELNAME " +
            "AND E.SCHEMANAME = '${schema}' and E.RELNAME = '${tableName}'";

    public String ALTER_USER_WHITELIST = "";
}
