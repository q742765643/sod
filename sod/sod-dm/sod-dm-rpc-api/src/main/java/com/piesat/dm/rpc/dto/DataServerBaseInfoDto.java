package com.piesat.dm.rpc.dto;

import lombok.Data;

import java.util.Date;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:17:36
 */
@Data
public class DataServerBaseInfoDto {
    private static final long serialVersionUID = 1L;

    private String id;

    private String dataCLassId;
    private String eleField;
    private String regionField;
    private String region;
    private String gribVersion;
    private String fieldType;//场类型
    private String processType;
    private String dataTime;
    private String timeUnit;
    private String spatialResolution;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
