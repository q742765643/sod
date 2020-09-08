package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 业务动态管理
 */
@Data
@Table(name = "T_SOD_PORTAL_DYN_MANAGE")
@Entity
public class DynManageEntity extends BaseEntity {

    /**
     * 标题
     */
    @Column(name="TITLE")
    private String title;

    /**
     * 正文
     */
    @Column(name="CONTENT",columnDefinition="TEXT",nullable=true)
    private String content;


    /**
     * 动态类型
     */
    @Column(name="NEWS_TYPE")
    private String newsType;


    /**
     * 发布状态
     */
    @Column(name="ISPUBLISHED")
    private String ispublished;

    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;

}
