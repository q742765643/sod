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
    private String policyName="";
    private String jobCron="";
    private int isConfiguration=0;
    private int triggerStatus=0;
    private String elapsedTime="";
    private String ddateTime="";



}

