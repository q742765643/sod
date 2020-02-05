package com.piesat.schedule.entity;

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
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name="type")
public class JobInfoLogEntity extends BaseEntity{
    @Column(name="job_id", length=50)
    private String jobId;
    @Column(name="executor_address", length=100)
    private String executorAddress;
    @Column(name="trigger_time")
    private long triggerTime;
    @Column(name="trigger_code")
    private Integer triggerCode;
    @Column(name="handle_time")
    private Date handleTime;
    @Column(name="handle_code",length = 1)
    private String handleCode;
    @Column(name="handle_msg", length=5000)
    private String handleMsg;
    @Column(name="elapsed_time")
    private long elapsedTime;
    @Transient
    private String type;


}

