package com.piesat.dm.rpc.dto.dataclass;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author cwh
 * @date 2020年 12月01日 15:17:34
 */
@Data
public class DataClassLogicDto extends BaseDto {
    /**
     * 存储编码
     */
    private String dataClassId;

    /**
     * 表id
     */
    private String tableId;

    /**
     * 子表id
     */
    private String subTableId;

    /**
     * 数据库Id
     */
    private String databaseId;
}
