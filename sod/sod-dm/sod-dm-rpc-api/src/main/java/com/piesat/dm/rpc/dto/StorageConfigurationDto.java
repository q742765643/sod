package com.piesat.dm.rpc.dto;

import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 13:48
 */
@Data
public class StorageConfigurationDto {

    private String classLogicId;
//    /**
//     * 逻辑库ID
//     */
//    private String logicId;
//
//    /**
//     * 物理库ID
//     */
//    private String databaseId;
//
//    /**
//     * 资料存储编码
//     */
//    private String dataClassId;

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
     * 数移清除配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    private Integer cleanIdentifier;


    /**
     * 数据迁移配置标识符
     * 1：已创建，2：未创建，3：不需配置
     */
    private Integer moveIdentifier;

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
     * 迁移任务ID
     */
    private String moveId;

    /**
     * 备份任务ID
     */
    private String backupId;


    private DataClassDto dataClassDto;
    private LogicDefineDto logicDefineDto;
    private DatabaseDto databaseDto;
    private DataLogicDto dataLogicDto;
    private DataTableDto dataTableDto;
    private TableColumnDto tableColumnDto;

}
