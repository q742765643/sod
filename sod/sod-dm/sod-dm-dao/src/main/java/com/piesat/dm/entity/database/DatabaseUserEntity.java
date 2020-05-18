package com.piesat.dm.entity.database;

import com.piesat.common.annotation.Excel;
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
    @Excel(name = "用户ID")
    @Column(name = "user_id")
    private String userId;

    /**
     * UP账户ID
     */
    @Excel(name = "账户ID")
    @Column(name = "database_up_id")
    private String databaseUpId;

    /**
     * UP账户密码
     */
    @Excel(name = "账户密码")
    @Column(name = "database_up_password")
    private String databaseUpPassword;

    /**
     * up账户IP
     */
    @Excel(name = "UP账户IP")
    @Column(name = "database_up_ip")
    private String databaseUpIp;

    /**
     * UP账户IP区间
     */
    @Excel(name = "UP账户区间")
    @Column(name = "database_up_ip_segment")
    private String databaseUpIpSegment;

    /**
     * UP账户描述
     */
    @Excel(name = "UP账户描述")
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
     * pdf路径
     */
    @Column(name = "pdf_path")
    private String pdfPath;
    /**
     * 审核人
     */
    @Column(name = "examiner")
    private String examiner;

    /**
     * 审核状态
     * 0待审  1已审  2拒绝
     */
    @Column(name = "examine_status")
    private String examineStatus;

    /**
     * 审核时间
     */
    @Excel(name = "审核时间")
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
    @Excel(name = "拒绝原因")
    @Column(name = "failure_reason")
    private String failureReason;

    /**
     * 记录
     */
    @Column(name = "remarks")
    private String remarks;
}
