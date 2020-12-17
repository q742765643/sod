package com.piesat.dm.core.model;

import lombok.Data;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author cwh
 * @date 2020年 12月09日 15:29:16
 */
@Data
public class SelectVo {
    private String schema;
    private String tableName;
    private String timeColumn;
    private String beginTime;
    private String endTime;
    private String orderBy;

    private List<String> columns;
    private String columnStr;
    private String where;

    public void setSchema(String schema) {
        this.schema = schema.toUpperCase();
    }

    public void setTableName(String tableName) {
        this.tableName = tableName.toUpperCase();
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
        if (columns != null && columns.size() > 0) {
            columnStr = columns.stream().collect(Collectors.joining(","));
        }
    }
}
