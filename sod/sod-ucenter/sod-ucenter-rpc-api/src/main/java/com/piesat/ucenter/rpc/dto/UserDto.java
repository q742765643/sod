package com.piesat.ucenter.rpc.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 11:04
 */
@Data
public class UserDto implements Serializable {
    private String id;

    private Integer version;

    private Date updateTime;

    private Date createTime;

    private String name;

    private String userName;

    private String password;

    private String addresses;

    private String emails;

    private String phoneNumbers;

    private String photos;

    /**
     * 用户角色id,应存入json串
     */
    private String roles;

    private String createUserId;

    private String lastUpdateUserId;

    private String status;

    private String certificates;

    private String description;
}
