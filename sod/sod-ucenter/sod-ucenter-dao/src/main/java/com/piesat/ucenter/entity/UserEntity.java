package com.piesat.ucenter.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 11:36
 */
@Data
@Table(name = "T_SOD_USER")
@Entity
public class UserEntity extends BaseEntity {
    //字段,不能为空,没有写默认可以为空
    @Column(name="name", length=255, nullable=false)
    private String name;

    @Column(name="user_name", length=255, nullable=false)
    private String userName;

    @Column(name="password", length=255)
    private String password;

    @Column(name="addresses", length=255)
    private String addresses;

    @Column(name="emails", length=255)
    private String emails;

    @Column(name="phone_numbers", length=255)
    private String phoneNumbers;

    @Column(name="photos", length=255)
    private String photos;

    /**
     * 用户角色id,应存入json串
     */
    @Column(name="roles", length=36, nullable=false)
    private String roles;

    @Column(name="create_user_id", length=36)
    private String createUserId;

    @Column(name="last_update_user_id", length=36)
    private String lastUpdateUserId;

    @Column(name="status", length=36, nullable=false)
    private String status;

    @Column(name="certificates", length=255, nullable=false)
    private String certificates;

    @Column(name="description", length=255)
    private String description;

}
