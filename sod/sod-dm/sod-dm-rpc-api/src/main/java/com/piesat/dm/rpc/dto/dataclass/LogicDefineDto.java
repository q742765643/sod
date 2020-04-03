package com.piesat.dm.rpc.dto.dataclass;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月21日 16:32:39
 */
@Data
public class LogicDefineDto {
    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * logic_id
     */
    private String logicFlag;

    /**
     * logic_name
     */
    private String logicName;

    /**
     * serial_number
     */
    private Integer serialNumber;

    /**
     * logic_desc
     */
    private String logicDesc;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;

    private List<LogicStorageTypesDto> logicStorageTypesEntityList;

    private List<LogicDatabaseDto> logicDatabaseEntityList;
}
