package com.piesat.schedule.rpc.dto;

import com.piesat.util.BaseDto;
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
    private String jobCron;		// 任务执行CRON表达式
    private String jobDesc;
    private String executorRouteStrategy;	// 执行器路由策略
    private String executorHandler;		    // 执行器，任务Handler名称
    private String executorParam;		    // 执行器，任务参数
    private String executorBlockStrategy;	// 阻塞处理策略
    private Integer executorTimeout;     		// 任务执行超时时间，单位秒
    private Integer executorFailRetryCount;		// 失败重试次数
    private Integer retryInterval;
    private Integer triggerStatus;		// 调度状态：0-停止，1-运行
    private long triggerLastTime;	// 上次调度时间
    private long triggerNextTime;	// 下次调度时间
    private String isAlarm;
    private String type;
}

