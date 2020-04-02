package com.piesat.dm.rpc.dto.dataclass;

import lombok.Data;

import java.util.Date;

/**
 * 数据用途与物理库关系
 *
 * @author cwh
 * @date 2020年 02月10日 15:44:39
 */
@Data
public class LogicDatabaseDto {

    private String id;

    private String logicId;

    private String databaseId;

    private String databaseName;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
