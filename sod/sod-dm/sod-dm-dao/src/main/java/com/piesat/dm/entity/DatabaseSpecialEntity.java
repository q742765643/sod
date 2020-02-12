package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 专题库管理
 *
 * @author wulei
 * @date 2020年 2月12日 15:12:47
 */
@Data
@Table(name = "T_SOD_DATABASE_SPECIAL")
@Entity
//@Proxy(lazy = false)
public class DatabaseSpecialEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 专题库名称
     */
    @Column(name = "sdb_name")
    private String sdbName;

    /**
     * 专题库图片
     */
    @Column(name = "sdb_img")
    private String sdbImg;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 用途
     */
    @Column(name = "use")
    private String use;

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
     * 拒绝原因
     */
    @Column(name = "failure_reason")
    private String failureReason;

    /**
     * 使用状态
     */
    @Column(name = "use_status")
    private String useStatus;

    /**
     * 数据库ID
     */
    @Column(name = "database_id")
    private String databaseId;

    /**
     * 数据库模式
     */
    @Column(name = "database_schema")
    private String databaseSchema;

    /**
     * 排序
     */
    @Column(name = "sort_no")
    private String sortNo;
}