package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户管理
 */
@Data
@Table(name = "T_SOD_PORTAL_DEPART")
@Entity
public class DepartManageEntity extends BaseEntity {

    /**
     * 部门唯一编号
     */
    @Column(name="DEPTUNICODE")
    String deptunicode;

    /**
     * 部门编码
     */
    @Column(name="DEPTCODE")
    String deptcode;

    /**
     * 部门名称
     */
    @Column(name="DEPTNAME")
    String deptname;

    /**
     * 类型 : 机构01 , 部门02
     */
    @Column(name="DEPTTYPE")
    String depttype;

    /**
     * 上级组织机构编码
     */
    @Column(name="PARENT_CODE")
    String parentCode;

    /**
     * 机构状态 : 01在用 02停用
     */
    @Column(name="STATUS")
    String status;

    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private Integer serialNumber;

    /**
     * 部门等级 01国家级 02直属单位 03省级 04市级  05县级
     */
    @Column(name="DEPT_LEVEL")
    String deptLevel;

    /**
     * 部门描述职责
     */
    @Column(name="DEPT_DSCR", length = 4001)
    String deptDscr;
}
