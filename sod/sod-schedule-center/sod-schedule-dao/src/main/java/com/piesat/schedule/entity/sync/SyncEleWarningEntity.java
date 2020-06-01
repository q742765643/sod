package com.piesat.schedule.entity.sync;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 键表要素表监控
 *
 * @author cwh
 * @date 2020年 05月11日 13:34:50
 */
@Data
@Entity
@Table(name = "T_SOD_JOB_SYNC_ELE_WARNING")
public class SyncEleWarningEntity extends BaseEntity {

    @Column(name="TASK_ID", length = 32)
    private String taskId;

    /**
     * 检查间隔(小时)
     */
    @Column(name="CHECK_INTERVAL")
    private Integer checkInterval;

    /**
     * 时间范围(小时)
     */
    @Column(name="TIME_LIMIT")
    private Integer timeLimit;

    /**
     * 最大差异条数
     */
    @Column(name="BIGGEST_DIFFERENCE")
    private Integer biggestDifference;
}
