package com.piesat.dm.core.template;

/**
 * Gbase 模板
 *
 * @author cwh
 * @date 2020年 09月17日 12:41:55
 */
public class TemplateGbase {
    public static final String CREATE_USER = "CREATE USER ${userName} IDENTIFIED BY '${password}'<#if whitelistStr??> hosts '${whitelistStr}'</#if>";

    public static final String QUERY_USER = "SELECT * FROM GBASE.USER WHERE  UPPER(USER) = UPPER('${userName}')";
    public static final String CREATE_SCHEMA = "CREATE DATABASE IF NOT EXISTS '${schema}' ";

    public static final String GRANT_ANY_TABLE = "GRANT ${grantStr} ON SCHEMA ${schema}.* TO ${userName} ";

    public static final String ALTER_RENAME_COLUMN = "ALTER TABLE ${schema}.${tableName} CHANGE ${oldColumnName} ${columnName} ${type}<#if length??> (${length}<#if precision??>,${precision}</#if>)</#if> ";
    public static final String ALTER_COLUMN_ATTR = "ALTER TABLE ${schema}.${tableName} CHANGE ${oldColumnName} ${columnName} ${type}<#if length??> (${length}<#if precision??>,${precision}</#if>)</#if> ";
    public static final String ALTER_COLUMN_SET_DEFAULT = "";
    public static final String ALTER_COLUMN_DROP_DEFAULT = "";
    public static final String ALTER_COLUMN_SET_NOTNULL = "";
    public static final String ALTER_COLUMN_DROP_NOTNULL = "";



    public static final String QUERY_INDEX = "SELECT TABLE_NAME,NON_UNIQUE,INDEX_SCHEMA,INDEX_NAME,GROUP_CONCAT(COLUMN_NAME) AS INDEX_COLUMN FROM " +
            "INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = '${tableName}' AND INDEX_SCHEMA = '${schema}' " +
            "GROUP BY TABLE_NAME,NON_UNIQUE,INDEX_SCHEMA,INDEX_NAME";
}
