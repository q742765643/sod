package com.piesat.dm.rpc.dto.dataclass;

import lombok.Data;

import java.util.Date;

/**
 * 资料用途分类
 *
 * @author cwh
 * @date 2019年 11月20日 17:04:05
 */
@Data
public class DataLogicDto {
//    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * data_class_id
     */
    private String dataClassId;

    /**
     * logic_id
     */
    private String logicFlag;

    /**
     * storage_type
     */
    private String storageType;

    /**
     * database_type
     */
    private String databaseId;

    private Boolean isComplete;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
