package com.piesat.schedule.sync.client.vo;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-14 11:28
 **/
@Data
public class SyncComVo {
    private long clearTime=0;
    private String conditions;
    private long startTime;
    private long endTime;
    private long count;
}

