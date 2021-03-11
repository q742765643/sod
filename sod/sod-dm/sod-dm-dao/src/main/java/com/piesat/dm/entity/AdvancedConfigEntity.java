package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/6 13:05
 */
@Data
@Entity
@Table(name = "T_SOD_ADVANCED_CONFIG")
public class AdvancedConfigEntity extends BaseEntity {

    /**
     * table_id
     */
    @Column(name = "table_id", length = 255, unique=true)
    private String tableId;

    /**
     * 存储结构创建标识
     * 1：已创建，2：未创建
     */
    @Column(name = "storage_define_identifier")
    private Integer storageDefineIdentifier;

    /**
     * 数据同步配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    @Column(name = "sync_identifier")
    private Integer syncIdentifier;

    /**
     * 数据清除配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    @Column(name = "clean_identifier")
    private Integer cleanIdentifier;


    /**
     * 数据迁移配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    @Column(name = "move_identifier")
    private Integer moveIdentifier;

    /**
     * 数据备份配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    @Column(name = "backup_identifier")
    private Integer backupIdentifier;

    /**
     * 数据归档配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    @Column(name = "archiving_identifier")
    private Integer archivingIdentifier;

    /**
     * 同步任务ID
     */
    @Column(name = "sync_id", length = 60)
    private String syncId;

    /**
     * 清楚任务ID
     */
    @Column(name = "clear_id", length = 60)
    private String clearId;

    /**
     * 迁移任务ID
     */
    @Column(name = "move_id", length = 60)
    private String moveId;

    /**
     * 备份任务ID
     */
    @Column(name = "backup_id", length = 60)
    private String backupId;

}
