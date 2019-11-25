package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

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
     * data_class_id
     */
    @Column(name = "data_class_id", length = 255, nullable = false)
    private String dataClassId;

    /**
     * class_name
     */
    @Column(name = "class_name", length = 255)
    private String className;

    /**
     * parent_class_id
     */
    @Column(name = "parent_id", length = 255)
    private String parentId;

    /**
     * serial_no
     */
    @Column(name = "serial_no", length = 36)
    private String serialNo;

    /**
     * type
     * 1为目录，2为资料
     */
    @Column(name = "type")
    private Integer type;

    /**
     * d_data_id
     */
    @Column(name = "d_data_id", length = 255)
    private String dDataId;

    /**
     * meta_data_name
     */
    @Column(name = "meta_data_name", length = 255)
    private String metaDataName;

    /**
     * access_control
     */
    @Column(name = "is_access")
    private Integer isAccess;

    /**
     * if_stop_use
     */
    @Column(name = "if_stop_use")
    private Integer ifStopUse;

    /**
     * frequency_type
     */
    @Column(name = "frequency_type")
    private Integer frequencyType;

    /**
     * is_all_line
     */
    @Column(name = "is_all_line")
    private Integer IsAllLine;

    /**
     * use_base_info
     */
    @Column(name = "use_base_info")
    private Integer UseBaseInfo;


}
