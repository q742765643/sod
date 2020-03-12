package com.piesat.schedule.entity;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-18 17:15
 **/

@Data
@Entity
@Table(name="T_SOD_JOB_INFO")
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name="type")
@DiscriminatorValue("JOB")
public class JobInfoEntity extends BaseEntity{
    @Column(name="job_group", length=50)
    private String jobGroup;		// 执行器主键ID
    @Excel(name = "执行策略")
    @Column(name="job_cron", length=255)
    private String jobCron;		// 任务执行CRON表达式
    @Column(name="job_desc", length=255)
    private String jobDesc;
    @Column(name="task_name", length=255)
    private String taskName;
    @Column(name="executor_route_strategy", length=50)
    private String executorRouteStrategy;	// 执行器路由策略
    @Column(name="executor_handler", length=255)
    private String executorHandler;		    // 执行器，任务Handler名称
    @Column(name="executor_param", columnDefinition = "TEXT")
    private String executorParam;		    // 执行器，任务参数
    @Column(name="executor_block_strategy", length=50)
    private String executorBlockStrategy;	// 阻塞处理策略
    @Column(name="executor_timeout", length=50)
    private Integer executorTimeout;     		// 任务执行超时时间，单位秒
    @Column(name="executor_fail_retry_count", length=50)
    private Integer executorFailRetryCount;		// 失败重试次数
    @Column(name="retry_interval", length=50)
    private Integer retryInterval;
    @Excel(name = "任务状态", readConverterExp = "0=未启动,1=启动")
    @Column(name="trigger_status", length=50)
    private Integer triggerStatus;		// 调度状态：0-停止，1-运行
    @Column(name="trigger_last_time", length=50)
    private long triggerLastTime;	// 上次调度时间
    @Column(name="trigger_next_time", length=50)
    private long triggerNextTime;	// 下次调度时间
    @Column(name="is_alarm", length=1)
    private String isAlarm;		// 是否告警
    @Transient
    private String type;		// 执行器主键ID
    @Column(name="is_redo", length=1)
    private String isRedo; //1为补做
    @Transient
    private String executorAddress;

}

