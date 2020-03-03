package com.piesat.schedule.rpc.dto;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 16:47
 **/
@Data
public class JobInfoDto extends BaseDto {
    private String jobGroup;		// 执行器主键ID
    @ApiModelProperty("CRON表达式")
    private String jobCron;		// 任务执行CRON表达式
    @ApiModelProperty("任务描述")
    private String jobDesc;
    @ApiModelProperty("任务名称")
    private String taskName;
    private String executorRouteStrategy;	// 执行器路由策略
    private String executorHandler;		    // 执行器，任务Handler名称
    private String executorParam;		    // 执行器，任务参数
    private String executorBlockStrategy;	// 阻塞处理策略
    @ApiModelProperty("超时时间")
    private Integer executorTimeout;// 任务执行超时时间，单位秒
    @ApiModelProperty("重试次数")
    private Integer executorFailRetryCount;		// 失败重试次数
    @ApiModelProperty("重试间隔时间")
    private Integer retryInterval;
    @ApiModelProperty("任务运行状态")
    private Integer triggerStatus;		// 调度状态：0-停止，1-运行
    private long triggerLastTime;	// 上次调度时间
    private long triggerNextTime;	// 下次调度时间
    @ApiModelProperty("是否告警")
    private String isAlarm;
    private String type;
    private String isRedo;
}

