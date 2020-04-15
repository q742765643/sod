package com.piesat.sod.system.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务代码管理
 * @author yaya
 * @description TODO
 * @date 2020/2/13 14:44
 */
@Data
@Entity
@Table(name = "T_SOD_SERVICE_CODE")
public class ServiceCodeEntity extends BaseEntity {

    /**
     * 服务代码
     */
    @Column(name="user_ele_code", length=30)
    private String userEleCode;

    /**
     * 字段编码
     */
    @Column(name="db_ele_code", length=25)
    private String dbEleCode;

    /**
     * 要素中文名称
     */
    @Column(name="ele_name", length=100)
    private String eleName;

    /**
     * 描述
     */
    @Column(name="description", length=255)
    private String description;

    /**
     *
     */
    @Column(name="has_sod", length=1)
    private String hasSod;

    /**
     *  单位
     */
    @Column(name="ele_unit", length=25)
    private String eleUnit;

    /**
     *  是否有标识代码表
     */
    @Column(name="is_code_param", length=1)
    private String isCodeParam;

    /**
     *  标识代码表
     */
    @Column(name="code_table_id", length=20)
    private String codeTableId;
}
