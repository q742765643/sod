package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 接口返回值
 */
@Data
@Table(name = "T_SOD_PORTAL_API_RES")
@Entity
public class ApiResEntity extends BaseEntity {

    /**
     * 接口ID
     */
    @Column(name="API_ID")
    private String apiId;

    /**
     * 返回值名称
     */
    @Column(name="RES_NAME")
    private String resName;


    /**
     * 返回值类型
     */
    @Column(name="RES_TYPE")
    private String resType;

    /**
     * 返回值描述
     */
    @Column(name="RES_DESC")
    private String resDesc;

    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;
}
