package com.piesat.ucenter.entity.system;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 15:23
 */
@Entity
@Data
@Table(name="T_SOD_USER")
public class UserEntity extends BaseEntity {
    /**
     * 部门id
     */
    @Column(name="dept_id", length=32)
    private String deptId;

    /**
     * 用户账号
     */
    @Column(name="user_name", length=30,nullable=false)
    private String userName;

    /**
     * 用户昵称
     */
    @Column(name="nick_name", length=30,nullable=false)
    private String nickName;

    /**
     * 用户类型（00系统用户）
     */
    @Column(name="user_type",columnDefinition = "varchar(2) default '00'")
    private String userType;

    /**
     * 用户邮箱
     */
    @Column(name="email",columnDefinition = "varchar(50) default ''")
    private String email;

    /**
     * 手机号码
     */
    @Column(name="phonenumber",columnDefinition = "varchar(11) default ''")
    private String phonenumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @Column(name="sex",columnDefinition = "varchar(1) default '0'")
    private String sex;

    /**
     * 头像地址
     */
    @Column(name="avatar",columnDefinition = "varchar(100) default ''")
    private String avatar;

    /**
     * 密码
     */
    @Column(name="password",columnDefinition = "varchar(100) default ''")
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Column(name="status",columnDefinition = "varchar(1) default '0'")
    private String status;

    /**
     * 最后登陆ip
     */
    @Column(name="login_ip",columnDefinition = "varchar(50) default '0'")
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @Column(name="login_date")
    private Date loginDate;


    /**
     * 备注
     */
    @Column(name="remark",length = 500)
    private String remark;


    /** 角色组 */
    @Transient
    private String[] roleIds;
    @Transient
    private DeptEntity dept;

    /** 角色对象 */
    @Transient
    private List<RoleEntity> roles;
}
