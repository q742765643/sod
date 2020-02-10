package com.piesat.dm.rpc.dto;

import lombok.Data;

import java.util.Date;

/**
 * 数据库管理账户
 *
 * @author cwh
 * @date 2019年 11月21日 19:12:47
 */
@Data
public class DatabaseUserDto {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String user_id;

    /**
     * UP账户ID
     */
    private String database_up_id;

    /**
     * UP账户密码
     */
    private String database_up_password;

    /**
     * up账户IP
     */
    private String database_up_ip;

    /**
     * UP账户IP区间
     */
    private String database_up_ip_segment;

    /**
     * UP账户描述
     */
    private String database_up_desc;

    /**
     * 申请数据库ID
     */
    private String apply_database_id;

    /**
     * 申请材料
     */
    private String apply_material;

    /**
     * 审核人
     */
    private String examiner;

    /**
     * 审核状态
     */
    private String examine_status;

    /**
     * 审核时间
     */
    private Date examine_time;

    /**
     * 审核通过的数据库ID
     */
    private String examine_database_id;

    /**
     * 拒绝原因
     */
    private String failure_reason;

    /**
     * 记录
     */
    private String remarks;
}
