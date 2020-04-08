package com.piesat.dm.entity.special;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/7 18:30
 */
@Data
@Table(name = "T_SOD_DATABASE_SPECIAL_ACCESS")
@Entity
public class DatabaseSpecialAccessEntity extends BaseEntity {

    /**
     * 专题库ID
     */
    @Column(name = "sdb_id")
    private String sdbId;

    /**
     * 访问权限
     *  1：读权限
     *  2：完整访问权限
     */
    @Column(name = "access_authority")
    private Integer accessAuthority;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 用途
     */
    @Column(name = "uses")
    private String uses;

    /**
     * 审核人
     */
    @Column(name = "examiner")
    private String examiner;

    /**
     * 审核状态
     * 1：待审核
     * 2：审核通过
     * 3：审核不通过
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
     * 1：未使用
     * 2：使用中
     * 3：撤销使用
     */
    @Column(name = "use_status")
    private String useStatus;

}
