package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SDK管理
 */
@Data
@Table(name = "T_SOD_PORTAL_SDK_DATA")
@Entity
public class SdkManageEntity extends BaseEntity {

    /**
     * sdk类型
     */
    @Column(name="SDK_TYPE")
    private String sdkType;

    /**
     * 开发语言
     */
    @Column(name="SDK_LANG")
    private String sdkLang;

    /**
     * 操作系统
     */
    @Column(name="SDK_SYS")
    private String sdkSys;

    /**
     * sdk包名称
     */
    @Column(name="SDK_JAR_NAME")
    private String sdkJarName;

    /**
     * sdk包地址
     */
    @Column(name="SDK_JAR_URL")
    private String sdkJarUrl;

    /**
     * sdk文档名称
     */
    @Column(name="SDK_DOC_NAME")
    private String sdkDocName;

    /**
     * sdk文档地址
     */
    @Column(name="SDK_DOC_URL")
    private String sdkDocUrl;

    /**
     * sdk描述
     */
    @Column(name="SDK_DESC")
    private String sdkDesc;

    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;

}
