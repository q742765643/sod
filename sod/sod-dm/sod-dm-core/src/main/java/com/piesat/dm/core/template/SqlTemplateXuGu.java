package com.piesat.dm.core.template;

import lombok.Data;

/**
 * XuGu 模板
 *
 * @author cwh
 * @date 2020年 09月17日 12:41:55
 */
@Data
public class SqlTemplateXuGu extends SqlTemplateComm {

    public String QUERY_SCHEMA = "SELECT * FROM DBA_SCHEMAS WHERE SCHEMA_NAME = '${schema}'";
    public String CREATE_USER = "CREATE USER ${userName} IDENTIFIED BY '${password}';GRANT CREATE ANY SCHEMA TO ${userName}";
    public String QUERY_TABLES = "SELECT DISTINCT B.TABLE_NAME FROM ALL_SCHEMAS A LEFT JOIN ALL_TABLES B ON A.SCHEMA_ID = B.SCHEMA_ID WHERE A.SCHEMA_NAME = '${schema}'";
    public String QUERY_ALL_TABLES = "SELECT DISTINCT TABLE_NAME FROM ALL_TABLES";

}
