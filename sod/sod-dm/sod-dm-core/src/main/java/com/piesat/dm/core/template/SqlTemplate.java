package com.piesat.dm.core.template;

import com.piesat.dm.core.constants.Constants;

/**
 * @author cwh
 * @date 2020年 12月04日 14:54:52
 */
public class SqlTemplate {
    public static final String HAVE_DATA = "SELECT * FROM ${schema}.${tableName} LIMIT 10";
    /**
     * 通用查询
     */
    public static final String QUERY = "SELECT ${columnStr} FROM ${schema}.${tableName} " +
            "<#if where?default(\"\")?trim?length gt 1> WHERE ${where}</#if> " +
            "<#if orderBy?default(\"\")?trim?length gt 1>ORDER BY ${orderBy}</#if>";
    /**
     * 开始结束时间查询
     */
    public static final String QUERY_BEGIN_END = "SELECT ${columnStr} FROM ${schema}.${tableName}" +
            "<#if timeColumn?default(\"\")?trim?length gt 1> WHERE " +
            "<#if beginTime?default(\"\")?trim?length gt 1>${timeColumn} >= '${beginTime}'</#if>" +
            "<#if beginTime?default(\"\")?trim?length gt 1&&endTime?default(\"\")?trim?length gt 1> AND </#if>" +
            "<#if endTime?default(\"\")?trim?length gt 1>${timeColumn} < '${endTime}'</#if>" +
            "</#if> <#if orderBy?default(\"\")?trim?length gt 1>ORDER BY ${orderBy}</#if>";
    /**
     * 虚谷分区查询
     */
    public static final String QUERY_PARTI = "SELECT PARTI_VAL " + Constants.RT + " FROM DBA_PARTIS WHERE " +
            "TABLE_ID=(SELECT TABLE_ID FROM DBA_TABLES A INNER JOIN DBA_SCHEMAS B ON A.SCHEMA_ID=B.SCHEMA_ID " +
            "WHERE TABLE_NAME='${tableName}' AND SCHEMA_NAME='${schema}') ORDER BY PARTI_NO ";

    /**
     * 默认建表模板
     */
    public static final String CREATE_TABLE_DISTRIBUTED =
            "CREATE TABLE IF NOT EXISTS \"${schema}\".\"${tableName}\"\n" +
            "(\n" +
            "<#list columnVos as columnVo>\n" +
            "  <#if columnVo_has_next>  \"${columnVo.columnName}\" ${columnVo.type}<#if columnVo.precision??> ${columnVo.precision}</#if><#if columnVo.isNull??> ${columnVo.isNull}</#if><#if columnVo.def??> DEFAULT ${columnVo.def}</#if><#if columnVo.comment??> COMMENT '${columnVo.comment}'</#if>,\n<#else>" +
            "  \"${columnVo.columnName}\" ${columnVo.type}<#if columnVo.precision??> ${columnVo.precision}</#if><#if columnVo.isNull??> ${columnVo.isNull}</#if><#if columnVo.def??> DEFAULT ${columnVo.def}</#if><#if columnVo.comment??> COMMENT '${columnVo.comment}'</#if> \n</#if>" +
            "</#list>\n" +
            " )<#if partiColumn?default(\"\")?length gt 1 >partition by range(${partiColumn}) interval 1 day partitions(\"PART1\" values less than ('1950-01-01 00:00:00'))</#if>;\n" +
            "<#list indexVos as indexVo>\n" +
            "${indexVo.indexType} ${indexVo.indexName} ON \"${schema}\".\"${tableName}\" (${indexVo.indexComment});\n" +
            "</#list>";
}
