package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 接口参数
 */
@Data
@Table(name = "T_SOD_PORTAL_API_PARAM")
@Entity
public class ApiParamEntity extends BaseEntity {

    /**
     * 接口ID
     */
    @Column(name="API_ID")
    private String apiId;

    /**
     * 参数名称
     */
    @Column(name="PARAM_NAME")
    private String paramName;

    /**
     * 参数类型
     */
    @Column(name="PARAM_TYPE")
    private String paramType;

    /**
     * 参数描述
     */
    @Column(name="PARAM_DESC")
    private String paramDesc;

    /**
     * 是否必选 Y:必选 N:不是必选
     */
    @Column(name="ISNEED")
    private String isneed;

    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;


}
