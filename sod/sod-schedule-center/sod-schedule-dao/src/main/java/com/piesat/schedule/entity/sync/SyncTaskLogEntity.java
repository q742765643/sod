package com.piesat.schedule.entity.sync;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/15 14:16
 */
@Data
@Entity
@Table(name = "T_SOD_JOB_SYNCTASK_INFO_LOG")
public class SyncTaskLogEntity extends BaseEntity {
    /**
     * 任务id
     */
    @Column(name="task_id", length = 32)
    private String taskId;

    /**
     * 日志级别
     */
    @Column(name="log_level")
    private Integer logLevel;

    /**
     * 日志时间
     */
    @Column(name="log_time")
    private Date logTime;

    /**
     * 任务状态
     */
    @Column(name="state")
    private Integer state;

    /**
     * 处理总记录数
     */
    @Column(name="counts")
    private Integer counts;

    /**
     * 插入记录数
     */
    @Column(name="insert_count")
    private Integer insertCount;

    /**
     * 更新记录数
     */
    @Column(name="update_count")
    private Integer updateCount;

    /**
     * 丢弃记录数
     */
    @Column(name="discard_count")
    private Integer discardCount;

    /**
     * 开始时间
     */
    @Column(name="begin_time")
    private Date beginTime;

    /**
     * 结束时间
     */
    @Column(name="end_time")
    private Date endTime;

    /**
     * 耗时
     */
    @Column(name="spend_time")
    private Long spendTime;

    /**
     * 错误信息
     */
    @Column(name="error", length = 1000)
    private String error;
}
