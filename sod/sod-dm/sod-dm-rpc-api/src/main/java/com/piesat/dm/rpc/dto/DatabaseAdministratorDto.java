package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * 数据库管理账户
 *
 * @author cwh
 * @date 2019年 11月21日 19:12:47
 */
@Data
public class DatabaseAdministratorDto {
    private static final long serialVersionUID = 1L;

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
     * 密码
     * pass_word
     */
    private String passWord;

    /**
     * 备注介绍
     * introduction
     */
    private String introduction;
}
