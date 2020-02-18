package com.piesat.dm.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 字段工具类
 *
 * @author cwh
 * @date 2020年 02月18日 09:41:49
 */
public class ColumnUtil {

    public ColumnUtil(String schema,String tableName,Boolean add, Column oldColumn, Column newColumn) {
        sql = new ArrayList<>();
    }

    private List<String> sql;

    private String schema;
    private String tableName;

    private Boolean add;

    private String oldName;
    private String newName;

    private String oldType;
    private String newType;

    private String oldPrecision;
    private String newPrecision;

    private String oldDef;
    private String newDef;

    private Boolean oldIsNull;
    private Boolean newIsNull;

}
