package com.piesat.dm.rpc.dto.database;

import lombok.Data;

import java.util.Date;

/**
 * 数据库管理账户
 *
 * @author cwh
 * @date 2019年 11月21日 19:12:47
 */
@Data
public class DatabaseAdministratorDto {
    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 数据库id
     * database_id
     */
    private String databaseId;

    /**
     * 用户名
     * user_name
     */
    private String userName;

    /**
     * 是否管理账户
     * is_manager
     */
    private Boolean isManager;
    /**
     * 密码
     * pass_word
     */
    private String passWord;

    /**
     * 备注介绍
     * introduction
     */
    private String introduction;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;

}
