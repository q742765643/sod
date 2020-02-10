package com.piesat.schedule.client.vo;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-10 18:06
 **/
@Data
public class BackupVo {
    private long backupTime=0;
    private long mistiming=0;
    private String conditions;
    private String secondConditions;
    private long backupTimeHis=0;
}

