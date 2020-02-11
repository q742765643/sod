package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 13:48
 */
@Data
public class StorageConfigurationDto {
    /**
     * 逻辑库ID
     */
    private String logicId;

    /**
     * 物理库ID
     */
    private String databaseId;

    /**
     * 资料存储编码
     */
    private String dataClassId;

    /**
     * 存储结构创建标识
     * 1：已创建，2：未创建
     */
    private Integer storageDefineIdentifier;

    /**
     * 数据同步配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    private Integer syncIdentifier;

    /**
     * 数据迁移清除配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    private Integer movecleanIdentifier;

    /**
     * 数据备份配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    private Integer backupIdentifier;

    /**
     * 数据归档配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    private Integer archivingIdentifier;

    /**
     * 同步任务ID
     */
    private String syncId;

    /**
     * 迁移清楚任务ID
     */
    private String clearId;

    /**
     * 备份任务ID
     */
    private String backupId;

}
