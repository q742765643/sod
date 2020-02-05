package com.piesat.ucenter.rpc.dto.system;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 17:02
 */
@Data
@ApiModel("user")
public class UserDto extends BaseDto {

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户类型（00系统用户）")
    private String userType;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("手机号码")
    private String phonenumber;

    @ApiModelProperty("用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty("头像地址")
    private String avatar;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("帐号状态（0正常 1停用）")
    private String status;


    @ApiModelProperty("最后登陆ip")
    private String loginIp;

    @ApiModelProperty("最后登陆时间")
    private Date loginDate;

    @ApiModelProperty("创建者")
    private String createBy;


    @ApiModelProperty("更新者")
    private String updateBy;

    @ApiModelProperty("备注")
    private String remark;

    private String appId;

    //@JsonIgnore
    private String params;

    private String[] roleIds;

    private DeptDto dept;

    private String loginLocation;

    private String browser;

    private String os;

    private String tokenId;

    private int operatorType;

    /**
     * 角色对象
     */
    private List<RoleDto> roles;
}
