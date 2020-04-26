package com.piesat.dm.rpc.dto.special;

import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 专题库管理
 *
 * @author wulei
 * @date 2020年 2月12日 15:12:47
 */
@Data
public class DatabaseSpecialDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    /**
     * 专题库名称
     */
    private String sdbName;

    /**
     * 专题库图片
     */
    private String sdbImg;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用途
     */
    private String uses;

    /**
     * 申请材料
     */
    private String applyMaterial;

    /**
     * 审核人
     */
    private String examiner;

    /**
     * 审核状态
     */
    private String examineStatus;

    /**
     * 审核时间
     */
    private Date examineTime;

    /**
     * 拒绝原因
     */
    private String failureReason;

    /**
     * 使用状态
     */
    private String useStatus;

    /**
     * 数据库ID
     */
    private String databaseId;

    /**
     * 数据库模式
     */
    private String databaseSchema;

    /**
     * 排序
     */
    private String sortNo;

	/**
     * 专题库简称
     */
    private String simpleName;

	/**
     * 数据库名称
     */
    private String databaseName;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 部门
     */
    private String department;
    /**
     * 联系方式
     */
    private String userPhone;

    private List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteList;
}
