package com.piesat.schedule.rpc.dto.sync;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/17 14:09
 */
@Data
public class SyncConfigDto {

    /**
     * id号
     */
    private Integer recordId;

    /**
     * 唯一约束键
     */
    private String uniqueKeys;

    /**
     * 分库分表设置
     */
    private String ifpatitions;

    /**
     * 分库分表键
     */
    private String partitionKeys;

    /**
     * 键值表
     */
    private String isKv;

    /**
     * 存储管理定义的四级编码，与表名一一对应
     */
    private String dDataId;

    /**
     * 目标表id
     */
    private String targetTableId;
}
