package com.piesat.dm.rpc.dto.dataclass;

import lombok.Data;

import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/17 19:19
 */
@Data
public class DataOnlineTimeDto {
    /**
     * 存储编码
     * data_class_id
     */
    private String dataClassId;

    /**
     * 资料最早时间
     */
    private Date beginTime;

    /**
     * 资料最新时间
     */
    private Date endTime;

    /**
     * 日期类型,today和end_time
     */
    private String endTimeFlag;

    /**
     * 0:未使用，1：使用中
     */
    private Integer using;

    /**
     * 最小阈值
     */
    private Date boundBeginTime;

    /**
     * 最大阈值
     */
    private Date boundEndTime;
}
