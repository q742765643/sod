package com.piesat.dm.entity.special;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 专题库管理资料
 *
 * @author wulei
 * @date 2020年 2月12日 17:12:47
 */
@Data
@Table(name = "T_SOD_DATABASE_SPECIAL_READ_WRITE")
@Entity
//@Proxy(lazy = false)
public class DatabaseSpecialReadWriteEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 专题库id
     */
    @Column(name = "sdb_id")
    private String sdbId;

    /**
     * 存储编码
     */
    @Column(name = "data_class_id")
    private String dataClassId;

    /**
     * 物理库Id
     */
    @Column(name = "database_id")
    private String databaseId;

    /**
     * 申请权限
     */
    @Column(name = "apply_authority")
    private Integer applyAuthority;

    /**
     * 授权权限
     */
    @Column(name = "empower_authority")
    private Integer empowerAuthority;

    /**
     * 审核人
     */
    @Column(name = "examiner")
    private String examiner;

    /**
     * 审核状态
     * 1：允许 2：禁止 3:待授权
     */
    @Column(name = "examine_status")
    private Integer examineStatus;

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
     * 区分自建还是申请
     */
    @Column(name = "data_type")
    private Integer dataType;

    /**
     * 分类id
     * 资料归属哪个分类下面
     */
    @Column(name = "type_id", length = 50)
    private String typeId;
}
