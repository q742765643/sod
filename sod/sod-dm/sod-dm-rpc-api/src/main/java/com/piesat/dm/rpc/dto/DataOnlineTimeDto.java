package com.piesat.dm.rpc.dto;

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
    private String data_class_id;

    /**
     * 资料最早时间
     */
    private Date begin_time;

    /**
     * 资料最新时间
     */
    private Date end_time;

    /**
     * 日期类型,today和end_time
     */
    private String end_time_flag;

    /**
     * 0:未使用，1：使用中
     */
    private Integer using;
}
