package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * 数据库基础库专题库
 *
 * @author cwh
 * @date 2019年 11月21日 18:03:14
 */
@Data
public class DatabaseDto {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库名称
     * database_name
     */
    private String databaseName;

    /**
     * 与数据库定义关联id
     * database_id
     */
    private String databaseId;

    /**
     * 数据库模式
     * schema_name
     */
    private String schemaName;

    /**
     * 数据库分类（物理库，专题库）
     * database_classify
     */
    private String databaseClassify;

    /**
     * 与申请表关联id
     * tdb_id
     */
    private String tdbId;
}
