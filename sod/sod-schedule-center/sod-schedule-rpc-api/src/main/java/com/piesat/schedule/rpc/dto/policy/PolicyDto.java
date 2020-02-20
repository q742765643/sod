package com.piesat.schedule.rpc.dto.policy;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-19 14:37
 **/
@Data
public class PolicyDto {
    private String taskId="";
    private String policyName="";
    private String jobCron="";
    private String isConfiguration="未配置";
    private String triggerStatus="";
    private String elapsedTime="";
    private String ddateTime="";
    private String sourceRepository="";
    private String sourceTable;



}

