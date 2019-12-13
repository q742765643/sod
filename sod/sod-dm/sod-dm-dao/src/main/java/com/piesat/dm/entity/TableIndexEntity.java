package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 表索引
 *
 * @author cwh
 * @date 2019年 11月21日 19:24:12
 */
@Data
@Table(name = "T_SOD_DATA_TABLE_INDEX")
@Entity
public class TableIndexEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 对应表id
     * table_id
     */
    @ManyToOne(targetEntity = DataTableEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id")
    private DataTableEntity tableId;

    /**
     * 索引类型
     * index_type
     */
    @Column(name = "index_type", length = 36, nullable = false)
    private String indexType;

    /**
     * 索引名称
     * index_name
     */
    @Column(name = "index_name", length = 36, nullable = false)
    private String indexName;

    /**
     * 索引字段
     * index_column
     */
    @Column(name = "index_column", length = 36, nullable = false)
    private String indexColumn;

}
