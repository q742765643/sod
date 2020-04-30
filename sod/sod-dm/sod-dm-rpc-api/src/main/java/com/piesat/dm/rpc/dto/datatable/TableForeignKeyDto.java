package com.piesat.dm.rpc.dto.datatable;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:02:54
 */
@Data
public class TableForeignKeyDto extends BaseDto {
    private String classLogicId;

    private String keyColumn;

    private String eleColumn;

    private Boolean isReal;
}
