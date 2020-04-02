package com.piesat.dm.entity.datatable;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:17:36
 */
@Data
@Table(name = "T_SOD_DATASERVICE_BASEINFOR")
@Entity
public class DataServerBaseInfoEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "data_class_id", length = 255)
    private String dataCLassId;

    //要素存储字段
    @Column(name = "ele_field", length = 255)
    private String eleField;

    //区域存储字段
    @Column(name = "region_field", length = 255)
    private String regionField;

    @Column(name = "region", length = 255)
    private String region;

    @Column(name = "grib_version", length = 255)
    private String gribVersion;

    @Column(name = "field_type", length = 255)
    private String fieldType;//场类型

    @Column(name = "process_type", length = 255)
    private String processType;

    @Column(name = "data_time", length = 255)
    private String dataTime;

    @Column(name = "time_unit", length = 255)
    private String timeUnit;

    @Column(name = "spatial_resolution", length = 255)
    private String spatialResolution;

}
