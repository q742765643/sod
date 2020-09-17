package com.piesat.dm.core.enums;

/**
 * 数据库字段长度
 */
public enum ColumnEnum {
    DATETIME("DATETIME", -1),
    TIMESTAMP("TIMESTAMP", -1),
    SMALLINT("SMALLINT", -1),
    BLOB("BLOB", -1),
    INT("INT", -1),
    BIGINT("BIGINT", -1),
    CLOB("CLOB", -1),
    CHAR("CHAR", 0),
    VARCHAR("VARCHAR", 0),
    DOUBLE("DOUBLE", 1),
    FLOAT("FLOAT", 1),
    NUMERIC("NUMERIC", 1),
    DECIMAL("DECIMAL", 1);
    // 成员变量
    private String name;
    private int length;

    private ColumnEnum(String name, int length) {
        this.name = name;
        this.length = length;
    }

    // 普通方法
    public static int getLength(String name) {
        for (ColumnEnum c : ColumnEnum.values()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c.length;
            }
        }
        return -2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public static void main(String[] args) {
        String ss = "datetime";
        String ss1 = "DATETIME";
        String s = ss.toUpperCase();
        System.out.println(s);
        int timestamp = ColumnEnum.getLength(s);
        System.out.println(timestamp);
String accuracy = "ss.1ss";
        boolean contains = accuracy.contains(".");
        System.out.println(contains);
        accuracy = accuracy.split("\\.")[0];
        System.out.println(accuracy);

    }
}
