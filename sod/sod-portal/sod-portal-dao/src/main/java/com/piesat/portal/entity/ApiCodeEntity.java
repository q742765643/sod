package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 接口样例代码
 */
@Data
@Table(name = "T_SOD_PORTAL_API_CODE")
@Entity
public class ApiCodeEntity extends BaseEntity {

    /**
     * 接口ID
     */
    @Column(name="API_ID")
    private String apiId;

    /**
     * 示例语言
     */
    @Column(name="CODE_LANG")
    private String codeLang;

    /**
     * 调用方式
     * 例如 : rest ,客户端
     */
    @Column(name="USE_TYPE")
    private String useType;

    /**
     * 返回值方式
     * 例如 : json , html
     */
    @Column(name="RES_TYPE")
    private String resType;

    /**
     * 参数示例
     */
    @Column(name="PARAM_CODE")
    private String paramCode;

    /**
     * 调用示例
     */
    @Column(name="USE_CODE")
    private String useCode;

    /**
     * 返回值示例
     */
    @Column(name="RES_CODE",columnDefinition="TEXT",nullable=true)
    private String resCode;

    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;

}
