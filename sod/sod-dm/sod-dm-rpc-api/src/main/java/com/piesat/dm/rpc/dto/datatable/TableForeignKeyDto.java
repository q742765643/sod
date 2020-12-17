package com.piesat.dm.rpc.dto.datatable;

import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:02:54
 */
@Data
public class TableForeignKeyDto extends BaseDto {
    /** 表id */
    private String tableId;

    /** 子表id */
    private String subTableId;

    /** 键字段 */
    private String keyColumn;

    /** 要素字段 */
    private String eleColumn;

    /** 是否真实存在 */
    private Boolean isReal;
}
