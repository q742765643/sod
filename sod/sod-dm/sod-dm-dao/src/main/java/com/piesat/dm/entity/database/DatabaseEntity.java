package com.piesat.dm.entity.database;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 数据库基础库专题库
 *
 * @author cwh
 * @date 2019年 11月21日 18:03:14
 */
@Data
@Table(name = "T_SOD_DATABASE")
@Entity
public class DatabaseEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库名称
     * database_name
     */
    @Column(name = "database_name", length = 36, nullable = false)
    private String databaseName;

    /**
     * 与数据库定义关联id
     * database_id
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "database_define_id")
    private DatabaseDefineEntity databaseDefine;

    /**
     * 数据库模式
     * schema_name
     */
    @Column(name = "schema_name", length = 36, nullable = false)
    private String schemaName;

    /**
     * 数据库分类（基础库，专题库）
     * database_classify
     */
    @Column(name = "database_classify", length = 36, nullable = false)
    private String databaseClassify;

    /**
     * 与申请表关联id
     * tdb_id
     */
    @Column(name = "tdb_id", length = 36)
    private String tdbId;

    /**
     * 是否管理账户
     * is_manager
     */
    @Column(name = "stop_use", columnDefinition = "Boolean default false")
    private Boolean stopUse;

    /**
     * 数据库等级 （1.基础数据库（适用于备份等操作））
     * database_level
     */
    @Column(name = "level")
    private Integer level;
}
