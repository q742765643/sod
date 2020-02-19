package com.piesat.dm.core.model;

import lombok.Data;

/**
 * 字段
 *
 * @author cwh
 * @date 2020年 02月18日 10:56:09
 */
@Data
public class Column {
    private String name;
    private String type;
    private String precision;
    private String def;
    private Boolean isNull;
}
