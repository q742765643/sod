package com.piesat.schedule.rpc.dto.sync;

import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/17 16:50
 */
@Data
public class SyncMappingDto {

    /**
     * 对应filter里的record_id
     */
    private String sourceTableId;

    /**
     * 源数据表名
     */
    private String sourceTableName;

    /**
     * 对应config里的record_id
     */
    private String targetTableId;

    /**
     * 目标表名
     */
    private String targetTableName;

    /**
     * 列映射关系
     */
    private String mapping;
}
