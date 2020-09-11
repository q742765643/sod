package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 文件管理
 */
@Data
@Table(name = "T_SOD_PORTAL_FILE_MANAGE")
@Entity
public class FileManageEntity extends BaseEntity {

    /**
     * 文件类型
     */
    @Column(name="FILE_TYPE")
    private String fileType;

    /**
     * 文件分类
     */
    @Column(name="FILE_CATEGORY")
    private String fileCategory;

    /**
     * 文件名
     */
    @Column(name="FILE_NAME")
    private String fileName;

    /**
     * 文件路径
     */
    @Column(name="FILE_PATH")
    private String filePath;

    /**
     * 文件后缀
     */
    @Column(name="FILE_SUFFIX")
    private String fileSuffix;

    /**
     * 文件描述
     */
    @Column(name="FILE_DESC")
    private String fileDesc;

    /**
     * 下载次数
     */
    @Column(name="DOWNLOADTIMES")
    private Integer downloadtimes;

    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;

    @Transient
    private String typeName;

    @Transient
    private String categoryName;
}
