package com.piesat.dm.entity.database;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据库管理账户
 *
 * @author cwh
 * @date 2019年 11月21日 19:12:47
 */
@Data
@Table(name = "T_SOD_DATABASE_ADMINISTRATOR")
@Entity
//@Proxy(lazy = false)
public class DatabaseAdministratorEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "database_id")
    private String databaseId;

    /**
     * 用户名
     * user_name
     */
    @Column(name = "user_name", length = 36, nullable = false)
    private String userName;

    /**
     * 是否管理账户
     * is_manager
     */
    @Column(name = "is_manager", columnDefinition = "Boolean")
    private Boolean isManager;

    /**
     * 密码
     * pass_word
     */
    @Column(name = "pass_word", length = 36, nullable = false)
    private String passWord;

    /**
     * 备注介绍
     * introduction
     */
    @Column(name = "introduction", length = 255)
    private String introduction;
}
