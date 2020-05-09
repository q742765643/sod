package com.piesat.dm.entity.datatable;

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

    //区域代码
    @Column(name = "area_id", length = 255)
    private String areaId;

    //要素存储代码
    @Column(name = "db_ele_name", length = 255)
    private String dbEleName;

    //要素服务编码
    @Column(name = "ele_service_id", length = 255)
    private String eleServiceId;

    //层次类型
    @Column(name = "level_type", length = 255)
    private Integer levelType;

    //GRIB版本
    @Column(name = "grib_version", length = 255)
    private Integer gribVersion;

    //要素中文名
    @Column(name = "ele_namecn", length = 255)
    private String eleNameCn;

    //资料时次
    @Column(name = "ele_hours", length = 255)
    private String eleHours;

    //时效单位
    @Column(name = "time_unit", length = 255)
    private String timeUnit;

    //层次列表
    @Column(name="level_list", columnDefinition="TEXT")
    private String levelList;

    //要素单位
    @Column(name = "ele_unit", length = 255)
    private String eleUnit;

    //预报时效列表
    @Column(name = "time_list", columnDefinition="TEXT")
    private String timeList;

    //空间分辨率
    @Column(name = "grid_pixel", length = 255)
    private String gridPixel;

    //要素长名
    @Column(name = "ele_long_name", length = 255)
    private String eleLongName;

    //场类型
    @Column(name = "field_type", length = 255)
    private Integer fieldType;            //场类型

    //加工过程类型
    @Column(name = "process_type", length = 255)
    private Integer processType;    //加工过程类型

    //序号
    @Column(name = "num", length = 255)
    private Integer num;

    //层次单位
    @Column(name = "level_unit", length = 255)
    private String levelUnit;

    //层次转换因子
    @Column(name = "scale_divisor", length = 255)
    private String scaleDivisor;

}
