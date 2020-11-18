package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据板块管理
 */
@Data
@Table(name = "T_SOD_PORTAL_DATA_PLATE")
@Entity
public class DataPlateEntity extends BaseEntity {

    /**
     * 四级编码
     */
    @Column(name="D_DATA_ID")
    private String dDataId;

    /**
     * 分类:1气象业务数据,2,地球科学数据,3社会行业数据
     */
    @Column(name="MODULE")
    private String module;

    /**
     * 数据名称
     */
    @Column(name="DATA_NAME")
    private String dataName;

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
