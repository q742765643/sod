package com.piesat.schedule.client.vo;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 17:13
 **/
@Data
public class StrategyVo {
    private String databaseId;
    private String ddataId;
    private String fileName;
    private String tableName;
    private String vtableName;
    private long beginTime;
    private long endTime;
    private String tableForeighFieId;
    private Integer isFull;
}

