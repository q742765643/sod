package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户管理
 */
@Data
@Table(name = "T_SOD_PORTAL_USER")
@Entity
public class UserManageEntity extends BaseEntity {

    @Id
    @Column(length = 50)
    private String id;

    /**
     * 真实姓名
     */
    @Column(name="USER_NAME")
    String userName;

    /**
     * 登录名
     */
    @Column(name="LOGIN_NAME")
    String loginName;

    /**
     * 密码
     */
    @Column(name="PASSWORD")
    String password;

    /**
     * 手机号
     */
    @Column(name="PHONE")
    String phone;

    /**
     * 座机
     */
    @Column(name="FIXEDPHONE")
    String fixedphone;

    /**
     * 性别 01男,02女
     */
    @Column(name="SEX")
    String sex;

    /**
     * 邮箱
     */
    @Column(name="EMAIL")
    String email;

    /**
     * 最后登录时间
     */
    @Column(name="LAST_LOGIN_TIME")
    Date lastLoginTime;

    /**
     * 部门编码
     */
    @Column(name="DEPTUNICODE")
    String deptunicode;

    /**
     * 是否审核:1:已激活,0:未审核,
     */
    @Column(name="ISCHECK")
    String ischeck;

    /**
     * 职位
     */
    @Column(name="POST")
    String post;

    /**
     * 职称
     */
    @Column(name="JOB_TITLE")
    String jobTitle;

    /**
     * 工作照路径
     */
    @Column(name="PICPATH")
    String picpath;

    /**
     * 用户级别 : 0:国家级,1:非国家级
     */
    @Column(name="USER_LEVEL")
    String userLevel;

    /**
     * 用户行为记录 : 1:一打开过提示信息
     */
    @Column(name="USER_ACT")
    String userAct;

    /**
     * 证书编码
     */
    @Column(name="CERT_CODE")
    String certCode;

    /**
     * 帐号状态：
     * 01在用
     * 02停用
     */
    @Column(name="STATUS")
    String status;

    /**
     * G码，格尔证书认证使用
     */
    @Column(name="G_CODE")
    String gCode;

    /**
     * 身份证号
     */
    @Column(name="ID_CARD")
    String idCard;

    /**
     * 公开方式
     */
    @Column(name="OPEN_TYPE")
    String openType;

    /** 角色组 */
    @Transient
    private String[] roleIds;
}
