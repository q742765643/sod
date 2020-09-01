package com.piesat.dm.entity.datatable;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月16日 15:34:12
 */
@Data
@Table(name = "T_SOD_DATA_TABLE_SHARDING")
@Entity
public class ShardingEntity extends BaseEntity {

    /**
     * table_id
     */
    @Column(name = "table_id", length = 255, nullable = false)
    private String tableId;

    /**
     * 列名
     * column_name
     */
    @Column(name = "column_name", length = 255)
    private String columnName;

    /**
     * 列名(分库键：0  分表键：1 )
     * column_name
     */
    @Column(name = "sharding_type", nullable = false)
    private Integer shardingType;
}
