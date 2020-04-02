package com.piesat.dm.rpc.dto.dataclass;

import lombok.Data;

import java.util.Date;

/**
 * 数据用途与存储类型对应关系
 *
 * @author cwh
 * @date 2019年 11月21日 17:11:14
 */
@Data
public class LogicStorageTypesDto {

    private String id;

    private String logicId;

    private String storageType;

    private String storageName;

    private String name;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
