package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 典型应用
 */
@Data
@Table(name = "T_SOD_PORTAL_TYPICAL_APP")
@Entity
public class TypicalAppEntity extends BaseEntity {

    /**
     * 类别 : 国家级:C , 省级:P , 公共云:CC
     */
    @Column(name="CLASS_CODE")
    private String classCode;

    /**
     * 图标路径
     */
    @Column(name="ICON")
    private String icon;

    /**
     * 应用名称
     */
    @Column(name="APP_NAME")
    private String appName;

    /**
     * 所属机构
     */
    @Column(name="ORG_NAME")
    private String orgName;

    /**
     * 跳转链接
     */
    @Column(name="URL")
    private String url;

    /**
     * 个数
     */
    @Column(name="NUM")
    private String num;


    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;

    /**
     * 是否显示
     * 是否显示 : Y 是 , N:否
     */
    @Column(name="ISSHOW")
    private String isshow;

}
