package com.piesat.dm.entity.dataapply;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "T_SOD_YUN_DATABASE_APPLY_LOG")
public class YunDatabaseApplyLogEntity extends BaseEntity {

    /**
     * 变更ID
     */
    @Column(name = "log_id", length = 50)
    private String logId;
    /**
     * 单位审核材料
     */
    @Column(name = "examine_material", length = 200)
    private String examineMaterial;
    /**
     * 数据库用途
     */
    @Column(name = "database_use", length = 200)
    private String databaseUse;

    /**
     * 审核状态
     * 包括：01-待审、02-已审、03-拒绝
     */
    @Column(name = "examine_status", length = 20)
    private String examineStatus;

    /**
     * 拒绝原因
     */
    @Column(name = "reject_reason", length = 50)
    private String rejectReason;
    /**
     * 操作用户id
     */
    @Column(name = "user_id", length = 50)
    private String userId;

    /**
     * 显示状态
     * 包括：1-申请、2-审核通过、3-管理员修改、
     */
    @Column(name = "view_status", length = 20)
    private String viewStatus;

    /**
     * 一级用户名
     */
//    @Column(name="user_name" ,length=50)
//    private String userName;
}
