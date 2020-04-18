package com.piesat.dm.entity.database;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 数据库管理账户
 *
 * @author cwh
 * @date 2019年 11月21日 19:12:47
 */
@Data
@Table(name = "T_SOD_DATABASE_USER")
@Entity
//@Proxy(lazy = false)
public class DatabaseUserEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * UP账户ID
     */
    @Column(name = "database_up_id")
    private String databaseUpId;

    /**
     * UP账户密码
     */
    @Column(name = "database_up_password")
    private String databaseUpPassword;

    /**
     * up账户IP
     */
    @Column(name = "database_up_ip")
    private String databaseUpIp;

    /**
     * UP账户IP区间
     */
    @Column(name = "database_up_ip_segment")
    private String databaseUpIpSegment;

    /**
     * UP账户描述
     */
    @Column(name = "database_up_desc")
    private String databaseUpDesc;

    /**
     * 申请数据库ID,database_define的id
     */
    @Column(name = "apply_database_id")
    private String applyDatabaseId;

    /**
     * 申请材料
     */
    @Column(name = "apply_material")
    private String applyMaterial;

    /**
     * 审核人
     */
    @Column(name = "examiner")
    private String examiner;

    /**
     * 审核状态
     */
    @Column(name = "examine_status")
    private String examineStatus;

    /**
     * 审核时间
     */
    @Column(name = "examine_time")
    private Date examineTime;

    /**
     * 审核通过的数据库ID
     */
    @Column(name = "examine_database_id")
    private String examineDatabaseId;

    /**
     * 拒绝原因
     */
    @Column(name = "failure_reason")
    private String failureReason;

    /**
     * 记录
     */
    @Column(name = "remarks")
    private String remarks;
}
