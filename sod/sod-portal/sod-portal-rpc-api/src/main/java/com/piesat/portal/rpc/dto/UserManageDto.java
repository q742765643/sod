package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class UserManageDto extends BaseDto {
    /**
     * 真实姓名
     */
    String userName;

    /**
     * 登录名
     */
    String loginName;

    /**
     * 密码
     */
    String password;

    /**
     * 手机号
     */
    String phone;

    /**
     * 座机
     */
    String fixedphone;

    /**
     * 性别 01男,02女
     */
    String sex;

    /**
     * 邮箱
     */
    String email;

    /**
     * 最后登录时间
     */
    Date lastLoginTime;

    /**
     * 部门编码
     */
    String deptunicode;

    /**
     * 部门名称
     */
    String deptName;

    /**
     * 是否审核:1:已审核,0:未审核
     */
    String ischeck;

    /**
     * 职位
     */
    String post;

    /**
     * 职称
     */
    String jobTitle;

    /**
     * 工作照路径
     */
    String picpath;

    /**
     * 用户级别 : 0:国家级,1:非国家级
     */
    String userLevel;

    /**
     * 用户行为记录 : 1:一打开过提示信息
     */
    String userAct;

    /**
     * 证书编码
     */
    String certCode;

    /**
     * 帐号状态：
     * 01在用
     * 02停用
     */
    String status;

    /**
     * G码，格尔证书认证使用
     */
    String gCode;

    /**
     * 身份证号
     */
    String idCard;

    /**
     * 公开方式
     */
    String openType;

    private String[] roleIds;
}
