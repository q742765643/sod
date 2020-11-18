package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 接口管理
 */
@Data
@Table(name = "T_SOD_PORTAL_API_MANAGE")
@Entity
public class ApiManageEntity extends BaseEntity {

    /**
     * 接口名称
     */
    @Column(name="API_NAME")
    private String apiName;

    /**
     * 接口属于哪个系统
     * 属于哪个系统:sod,cms,music,dpl
     */
    @Column(name="API_SYS")
    private String apiSys;

    /**
     * 接口分类
     */
    @Column(name="API_TYPE")
    private String apiType;

    /**
     * 调用方法:G:get P:post A:get/post
     */
    @Column(name="API_HTTPTYPE")
    private String apiHttptype;


    /**
     * 调用样例
     */
    @Column(name="API_EXAMPLE")
    private String apiExample;

    /**
     * 发布状态:1/发布 , 0/未发布
     */
    @Column(name="STATUS")
    private String status;

    /**
     * 备注
     */
    @Column(name="REMARK")
    private String remark;

    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;

    /**
     * 接口描述
     */
    @Column(name="API_DESC")
    private String apiDesc;

}
