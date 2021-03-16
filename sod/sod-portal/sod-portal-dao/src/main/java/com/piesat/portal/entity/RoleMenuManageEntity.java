package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.UUIDEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name="T_SOD_PORTAL_ROLE_MENU")
public class RoleMenuManageEntity extends UUIDEntity {
    /** 角色ID */
    @Column(name="ROLE_ID", length=32)
    private String roleId;

    /** 菜单ID */
    @Column(name="MENU_ID", length=32)
    private String menuId;
}
