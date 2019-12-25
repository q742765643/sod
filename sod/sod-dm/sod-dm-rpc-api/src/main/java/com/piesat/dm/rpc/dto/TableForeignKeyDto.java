package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:02:54
 */
@Data
public class TableForeignKeyDto {
    private String tableId;

    private String keyColumn;

    private String eleColumn;

    private Boolean isReal;
}
