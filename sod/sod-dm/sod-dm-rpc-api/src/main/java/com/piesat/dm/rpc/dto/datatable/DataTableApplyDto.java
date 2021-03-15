package com.piesat.dm.rpc.dto.datatable;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author cwh
 * @date 2020年 12月01日 15:19:19
 */
@Data
public class DataTableApplyDto extends BaseDto {
    private String databaseId;

    /**
     * 表存储类型
     */
    private String storageType;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表子类型（键表、要素表）
     */
    private String tableType;

    /**
     * 表描述
     */
    private String tableDesc;

    /**
     * 中文名
     */
    private String nameCn;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 申请人
     */
    private String userId;

    /**
     * 状态
     */
    private String status;

    /**
     * 描述
     */
    private String remark;
}
