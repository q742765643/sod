package com.piesat.schedule.entity;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 14:47
 **/
@Data
@Entity
@Table(name="T_SOD_JOB_INFO_LOG")
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name="type")
public class JobInfoLogEntity extends BaseEntity{
    @Column(name="job_id", length=50)
    private String jobId;
    @Column(name="task_name", length=255)
    private String taskName;
    @Excel(name = "执行地址")
    @Column(name="executor_address", length=100)
    private String executorAddress;
    @Column(name="trigger_time")
    private long triggerTime;
    @Column(name="trigger_code")
    private Integer triggerCode;
    @Excel(name = "时间执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    @Column(name="handle_time")
    private Date handleTime;
    @Excel(name = "执行状态",readConverterExp = "0=运行中,1=成功,2=失败")
    @Column(name="handle_code",length = 1)
    private String handleCode;
    @Excel(name = "执行过程")
    @Column(name="handle_msg", columnDefinition = "TEXT")
    private String handleMsg;
    @Excel(name = "执行耗时")
    @Column(name="elapsed_time")
    private long elapsedTime;


}

