package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import java.util.Date;

/**
 * 数据服务配置
 *
 * @author cwh
 * @date 2020年 02月12日 15:35:42
 */
@Data
public class DataServerConfigDto {
    private static final long serialVersionUID = 1L;

    private String id;

    private String dataServiceId;
    private String areaId;
    private String dbEleName;
    private String eleServiceId;
    private Integer levelType;
    private Integer gribVersion;
    private String eleNameCn;
    private String eleHours;
    private String timeUnit;
    private String levelList;
    private String eleUnit;
    private String timeList;
    private String gridPixel;
    private String eleLongName;
    private Integer fieldType;            //场类型
    private Integer processType;    //加工过程类型
    private Integer num;
    private String levelUnit;
    private String scaleDivisor;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
