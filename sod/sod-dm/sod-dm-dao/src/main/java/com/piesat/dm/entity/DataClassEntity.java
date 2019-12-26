package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月20日 17:02:58
 */
@Data
@Table(name = "T_SOD_DATA_CLASS")
@Entity
public class DataClassEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 存储编码
     * data_class_id
     */
    @Column(name = "data_class_id", length = 255, nullable = false)
    private String dataClassId;

    /**
     * 名称
     * class_name
     */
    @Column(name = "class_name", length = 255)
    private String className;

    /**
     * 父节点id
     * parent_class_id
     */
    @Column(name = "parent_id", length = 255)
    private String parentId;

    /**
     * 排序字段
     * serial_no
     */
    @Column(name = "serial_no", length = 36)
    private Integer serialNo;

    /**
     * type
     * 1为目录，2为资料
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 四级编码
     * d_data_id
     */
    @Column(name = "d_data_id", length = 255)
    private String dDataId;

    /**
     * 数据名称
     * meta_data_name
     */
    @Column(name = "meta_data_name", length = 255)
    private String metaDataName;

    /**
     * access_control
     */
    @Column(name = "is_access", columnDefinition="integer DEFAULT 1 COMMENT '访问控制（1、通过，2、不通过）'")
    private Integer isAccess;

    /**
     * if_stop_use
     */
    @Column(name = "if_stop_use", columnDefinition="Boolean DEFAULT false COMMENT '是否停用'")
    private Boolean ifStopUse;

    /**
     * 频率（无用）
     * frequency_type
     */
    @Column(name = "frequency_type")
    private Integer frequencyType;

    /**
     * is_all_line
     */
    @Column(name = "is_all_line", columnDefinition="integer DEFAULT 1 COMMENT '是否在线（1、在线，2、不在线）'")
    private Integer isAllLine;

    /**
     * use_base_info
     */
    @Column(name = "use_base_info", columnDefinition="integer DEFAULT 1 COMMENT '是否使用基础信息配置（1、使用，2、不使用）'")
    private Integer useBaseInfo;


}
