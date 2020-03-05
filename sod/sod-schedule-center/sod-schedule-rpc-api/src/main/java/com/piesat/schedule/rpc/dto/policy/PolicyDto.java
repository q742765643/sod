package com.piesat.schedule.rpc.dto.policy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-19 14:37
 **/
@Data
public class PolicyDto {
    @ApiModelProperty("任务ID")
    private String taskId="";
    @ApiModelProperty("策略名称")
    private String policyName="";
    @ApiModelProperty("执行策略")
    private String jobCron="";
    @ApiModelProperty("是否配置")
    private String isConfiguration="未配置";
    @ApiModelProperty("是否启动")
    private String triggerStatus="";
    @ApiModelProperty("执行耗时")
    private String elapsedTime="";
    @ApiModelProperty("资料时间")
    private String ddateTime="";
    @ApiModelProperty("下5次执行时间")
    private List<String> nextTime=new ArrayList<>();
    @ApiModelProperty("源库 同步专有")
    private String sourceRepository="";
    @ApiModelProperty("源表 同步专有")
    private String sourceTable="";



}

