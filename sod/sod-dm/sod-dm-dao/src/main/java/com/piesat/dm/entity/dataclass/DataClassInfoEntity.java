package com.piesat.dm.entity.dataclass;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Table(name = "T_SOD_DATA_CLASS_INFO")
@Entity
public class DataClassInfoEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 存储编码
     * data_class_id
     */
    @Column(name = "data_class_id", length = 255, nullable = false)
    private String dataClassId;

    /**
     * 数据名称（主要用于中间数据）
     * data_name
     */
    @Column(name = "data_name", length = 255)
    private String dataName;

    /**
     * 资料状态
     * status
     */
    @Column(name = "status", length = 255)
    private Integer status;

    /**
     * 数据等级
     * data_level
     */
    @Column(name = "data_level")
    private Integer dataLevel;

    /**
     * 数据描述
     * data_desc
     */
    @Column(name = "data_desc")
    private String dataDesc;


    /**
     * 是否归档（0：否，1：是）
     * is_archive
     */
    @Column(name = "is_archive")
    private Integer isArchive;

    /**
     * 发布状态
     * published
     */
    @Column(name = "published")
    private Integer published;


    /**
     * 存储目录
     * dir
     */
    @Column(name = "dir")
    private String dir;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 数据类型（0：业务数据，1：业务中间数据）
     * data_type
     */
    @Column(name = "data_type")
    private Integer dataType;

    /**
     * 序号
     */
    @Column(name = "sn")
    private Integer sn;

}
