package com.piesat.dm.entity.dataapply;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author yaya
 * @description portal端的资源申请
 * @date 2020/3/30 11:59
 */
@Data
@Entity
@Table(name = "DATA_RESOURCE_APPLY")
public class DataResourceApplyEntity extends BaseEntity {
    /**
     * 用户ID
     */
    @Column(name = "user_id", length = 36)
    private String userId;

    /**
     * 资源类型
     */
    @Column(name = "src_type", length = 50)
    private String srcType;

    /**
     * 资源名称
     */
    @Column(name = "src_name", length = 50)
    private String srcName;

    /**
     * 资源id
     */
    @Column(name = "src_id", length = 36)
    private String srcId;

    /**
     * 审核人
     */
    @Column(name = "examiner", length = 36)
    private String examiner;

    /**
     * 审核状态
     * 01未审核;02通过;03驳回
     */
    @Column(name = "examine_status", length = 2)
    private String examineStatus;

    /**
     * 审核时间
     */
    @Column(name = "examine_time")
    private Date examineTime;

    /**
     * 用途
     */
    @Column(name = "uses")
    private String uses;

    /**
     * 机构编号
     */
    @Column(name = "orgcode")
    private String orgcode;

    /**
     * 文件路径
     */
    @Column(name = "file_path")
    private String filePath;

    /**
     * 拒绝原因
     */
    @Column(name = "failure_reason")
    private String failureReason;
}
