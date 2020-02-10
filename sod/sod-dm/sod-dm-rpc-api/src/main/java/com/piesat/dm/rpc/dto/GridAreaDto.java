package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:17:36
 */
@Data
public class GridAreaDto {

    private static final long serialVersionUID = 1L;

    private Integer dataServiceId;

    private Integer areaId;

    private Integer areaRegionDesc;

}
