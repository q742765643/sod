package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务信息配置
 *
 * @author cwh
 * @date 2020年 02月12日 15:44:37
 */
@Data
@Table(name = "T_SOD_DATASERVICE_CONFIG")
@Entity
public class DataServerConfigEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "data_service_id", length = 255)
    private String dataServiceId;

    @Column(name = "area_id", length = 255)
    private String areaId;

    @Column(name = "db_ele_name", length = 255)
    private String dbEleName;

    @Column(name = "ele_service_id", length = 255)
    private String eleServiceId;

    @Column(name = "level_type", length = 255)
    private Integer levelType;

    @Column(name = "grib_version", length = 255)
    private Integer gribVersion;

    @Column(name = "ele_namecn", length = 255)
    private String eleNameCn;

    @Column(name = "ele_hours", length = 255)
    private String eleHours;

    @Column(name = "time_unit", length = 255)
    private String timeUnit;

    @Column(name = "level_list", length = 255)
    private String levelList;

    @Column(name = "ele_unit", length = 255)
    private String eleUnit;

    @Column(name = "time_list", length = 255)
    private String timeList;

    @Column(name = "grid_pixel", length = 255)
    private String gridPixel;

    @Column(name = "ele_long_name", length = 255)
    private String eleLongName;

    @Column(name = "field_type", length = 255)
    private Integer fieldType;            //场类型

    @Column(name = "process_type", length = 255)
    private Integer processType;    //加工过程类型

    @Column(name = "num", length = 255)
    private Integer num;

    @Column(name = "level_unit", length = 255)
    private String levelUnit;

    @Column(name = "scale_divisor", length = 255)
    private String scaleDivisor;

}
