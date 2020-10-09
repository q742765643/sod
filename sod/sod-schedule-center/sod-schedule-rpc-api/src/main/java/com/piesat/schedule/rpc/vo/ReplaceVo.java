package com.piesat.schedule.rpc.vo;

import lombok.Data;

import java.util.Set;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 18:10
 **/
@Data
public class ReplaceVo {
    private String databaseId;
    private String dataClassId;
    private String ddataId;
    private long backupTime;
    private String msg;
    private Set timeSet;
}

