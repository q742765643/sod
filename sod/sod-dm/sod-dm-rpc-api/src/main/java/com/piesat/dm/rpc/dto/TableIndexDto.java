package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * 表索引
 *
 * @author cwh
 * @date 2019年 11月21日 19:24:12
 */
@Data
public class TableIndexDto {
    private static final long serialVersionUID = 1L;

    /**
     * 对应表id
     * table_id
     */
    private String tableId;

    /**
     * 索引类型
     * index_type
     */
    private String indexType;

    /**
     * 索引名称
     * index_name
     */
    private String indexName;

    /**
     * 索引字段
     * index_column
     */
    private String indexColumn;

}
