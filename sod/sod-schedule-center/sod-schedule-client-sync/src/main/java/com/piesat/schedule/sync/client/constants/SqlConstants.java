package com.piesat.schedule.sync.client.constants;

/**
 * sql模板
 * @author cwh
 * @date 2020年 08月27日 15:31:01
 */
public class SqlConstants {

    public static final String GET_COLUMN_SQL = "SELECT * FROM %s WHERE 1 = 2";

    public static final String QUERY_SQL = "SELECT %s FROM %s WHERE %s";

    public static final String WHERE_SQL = " %s %s '%s' ";

}
