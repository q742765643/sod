package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 算法板块管理
 */
@Data
@Table(name = "T_SOD_PORTAL_DPL_PLATE")
@Entity
public class DplPlateEntity extends BaseEntity {


    /**
     * 分类编号
     */
    @Column(name="CLASSIFY_ID")
    private String classifyId;

    /**
     * 算法分类名称
     */
    @Column(name="ALG_NAME")
    private String algName;

    /**
     * 图标
     */
    @Column(name="ICON", columnDefinition="TEXT",nullable=true)
    private String icon;

    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private Integer serialNumber;

    /**
     * 是否显示
     * 是否显示 : Y 是 , N:否
     */
    @Column(name="ISSHOW")
    private String isshow;

}
