package com.piesat.dm.core.constants;

/**
 * @author cwh
 * @date 2020年 12月04日 14:54:52
 */
public class Constants {

    public static final String POINT = ".";
    public static final String EMPTY = "";
    public static final String APOSTROPHE = "'";
    public static final String DOUBLE_QUOTES = "\"";
    public static final String SPACE = " ";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    public static final String D_DATETIME = "D_DATETIME";
    public static final String COUNT = "COUNT(*) ";
    public static final String RT = "RT";
    public static final String MIN = "MIN(%s) ";
    public static final String MAX = "MAX(%s) ";
    public static final String ZERO = "0";
    public static final String PERCENT = "%";
    public static final String STAR = "*";
    public static final String[] TIME_TYPES = new String[]{"DATE","DATETIME","TIMESTAMP"};

    public static final String LT = " < ";
    public static final String LE = " <= ";
    public static final String EQ = " = ";
    public static final String NE = " != ";
    public static final String GE = " >= ";
    public static final String GT = " > ";

    public static final String FIELD_LENGTH = "(%s, %s)";
    public static final String FIELD_LENGTH_ONLY = "(%s)";

    public static final String N = "N";

    /**
     * 索引类型
     */
    public static final String UNIQUE  = "UNIQUE";
    public static final String INDEX = "INDEX";
    public static final String FULLTEXT = "FULLTEXT";
    public static final String PRIMARY_KEY = "PRIMARY_KEY";

}
