package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import java.util.Date;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月16日 15:52:04
 */
@Data
public class ShardingDto {
    private static final long serialVersionUID = 1L;

    private String id;
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

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
