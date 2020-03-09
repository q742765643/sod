package com.piesat.sod.system.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务代码定义
 * @author yaya
 * @description TODO
 * @date 2020/3/9 13:45
 */
@Data
@Entity
@Table(name = "T_SOD_SERVICE_CODE_DEFINE")
public class ServiceCodeDefineEntity extends BaseEntity {

    /**
     * 要素服务代码
     */
    @Column(name="user_fcst_ele", length=50)
    private String userFcstEle;

    /**
     * 要素存储短名
     */
    @Column(name="db_fcst_ele", length=50)
    private String dbFcstEle;

    /**
     * 要素名（要素长名）
     */
    @Column(name="ele_property_name", length=150)
    private String elePropertyName;

    /**
     * 要素单位
     */
    @Column(name="ele_unit", length=100)
    private String eleUnit;

    /**
     * 中文名称
     */
    @Column(name="ele_name", length=255)
    private String eleName;


}
