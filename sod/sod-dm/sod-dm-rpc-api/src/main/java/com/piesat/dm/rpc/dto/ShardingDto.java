package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月16日 15:52:04
 */
@Data
public class ShardingDto {
    /**
     * table_id
     */
    private String tableId;

    /**
     * 列名
     * column_name
     */
    private String columnName;

    /**
     * 列名(分库键：0  分表键：1 )
     * column_name
     */
    private Integer shardingType;
}
