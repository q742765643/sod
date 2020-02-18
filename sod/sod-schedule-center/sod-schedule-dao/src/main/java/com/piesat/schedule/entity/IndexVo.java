package com.piesat.schedule.entity;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-17 16:58
 **/
@Data
public class IndexVo {
    private String table;
    private String newPath;
    private String dfileId;
    private String ddateTime;
    private String conditions;
}

