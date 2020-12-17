package com.piesat.dm.core.model;

import com.piesat.dm.core.constants.Constants;
import lombok.Data;

/**
 * 字段
 *
 * @author cwh
 * @date 2020年 02月18日 10:56:09
 */
@Data
public class ColumnVo {
    private String schema;
    private String tableName;
    private String oldColumnName;
    private String columnName;
    private String type;
    /**
     * 精度
     */
    private String precision;
    /**
     * "" 或者 "DEFAULT '*'"
     */
    private String def;
    /**
     * "" 或者 "NOT NULL"
     */
    private String isNull;
    private String comment;

    public void format(){
        if (!Constants.D_DATETIME.equals(type)){
            def = Constants.APOSTROPHE + def + Constants.APOSTROPHE;
        }
    }
}
