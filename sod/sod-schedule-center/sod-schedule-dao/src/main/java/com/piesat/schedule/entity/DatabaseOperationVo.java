package com.piesat.schedule.entity;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-14 16:53
 **/
@Data
public class DatabaseOperationVo {
    private String vtable;
    private String ktable;
    private String conditions;
    private long limit;
    private String fk;
    private String fkconditions;
    private String minTime;
}

