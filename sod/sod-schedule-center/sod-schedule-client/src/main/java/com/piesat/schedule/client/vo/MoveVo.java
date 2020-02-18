package com.piesat.schedule.client.vo;

import lombok.Data;

/**
 * @program: sod
 * @description: 迁移
 * @author: zzj
 * @create: 2020-02-17 13:53
 **/
@Data
public class MoveVo {
    private long moveTime;
    private long clearTime;
    private long archiveTime;
    private long moveCount;
    private long clearCount;
    private long archiveCount;
    private long startTime;
    private long endTime;
    private String moveConditions;
    private String clearConditions;
    private String archiveConditions;


}

