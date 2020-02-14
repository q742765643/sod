package com.piesat.dm.rpc.dto;

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
public class DatabaseSpecialReadWriteDto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 专题库id
     */
    private String sdbId;

    /**
     * 存储编码
     */
    private String dataClassId;

    /**
     * 物理库Id
     */
    private String databaseId;

    /**
     * 申请权限
     */
    private Integer applyAuthority;

    /**
     * 授权权限
     */
    private Integer empowerAuthority;

    /**
     * 审核人
     */
    private String examiner;

    /**
     * 审核状态
     */
    private Integer examineStatus;

    /**
     * 审核时间
     */
    private Date examineTime;

    /**
     * 拒绝原因
     */
    private String failureReason;

    /**
     * 区分自建还是申请
     */
    private Integer dataType;

    private String userId;
}