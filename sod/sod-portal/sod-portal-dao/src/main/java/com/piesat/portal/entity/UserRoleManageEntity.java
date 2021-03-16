package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.UUIDEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name="T_SOD_PORTAL_USER_ROLE")
public class UserRoleManageEntity extends UUIDEntity {

    /** 用户ID */
    @Column(name="USER_ID", length=32)
    private String userId;

    /** 角色ID */
    @Column(name="ROLE_ID", length=32)
    private String roleId;
}
