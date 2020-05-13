package com.piesat.dm.rpc.dto.database;

import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.Date;

/**
 * 数据库管理账户
 *
 * @author cwh
 * @date 2019年 11月21日 19:12:47
 */
@Data
public class DatabaseUserDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * UP账户ID
     */
    private String databaseUpId;

    /**
     * UP账户密码
     */
    private String databaseUpPassword;

    /**
     * up账户IP
     */
    private String databaseUpIp;

    /**
     * UP账户IP区间
     */
    private String databaseUpIpSegment;

    /**
     * UP账户描述
     */
    private String databaseUpDesc;

    /**
     * 申请数据库ID
     */
    private String applyDatabaseId;

    /**
     * 申请材料pdf
     */
    private String pdfPath;

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
     * 审核通过的数据库ID
     */
    private String examineDatabaseId;

    /**
     * 拒绝原因
     */
    private String failureReason;

    /**
     * 记录
     */
    private String remarks;

    /**
     * 申请数据库名称
     */
    private String applyDatabaseName;

    /**
     * 创建时间
     */
    private Date createTime;

    private String userName;

    private String deptName;

    private String tutorPhone;

    private String phonenumber;
}
