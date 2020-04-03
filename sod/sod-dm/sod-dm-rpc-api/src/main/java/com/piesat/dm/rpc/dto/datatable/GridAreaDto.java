package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import java.util.Date;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:17:36
 */
@Data
public class GridAreaDto {
    private static final long serialVersionUID = 1L;

    private String id;

    private String dataServiceId;

    private String areaId;

    private String areaRegionDesc;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
