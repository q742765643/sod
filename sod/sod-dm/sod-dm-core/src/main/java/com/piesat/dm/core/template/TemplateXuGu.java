package com.piesat.dm.core.template;

/**
 * XuGu 模板
 *
 * @author cwh
 * @date 2020年 09月17日 12:41:55
 */
public class TemplateXuGu {

    public static final String QUERY_SCHEMA = "SELECT * FROM DBA_SCHEMAS WHERE SCHEMA_NAME = '${schema}'";
    public static final String CREATE_USER = "CREATE USER ${userName} IDENTIFIED BY '${password}';GRANT CREATE ANY SCHEMA TO ${userName}";

}
