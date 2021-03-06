package com.piesat.dm.rpc.dto;

import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 13:48
 */
@Data
public class AdvancedConfigDto {

    private String tableId;

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
    private SchemaDto schemaDto;
    private DataLogicDto dataLogicDto;
    private DataTableInfoDto dataTableInfoDto;
    private TableColumnDto tableColumnDto;

}
