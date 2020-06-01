package com.piesat.dm.rpc.dto.dataclass;

import lombok.Data;

import java.util.Date;

/**
 * 规范信息
 *
 * @author cwh
 * @date 2020年 02月12日 16:56:04
 */
@Data
public class DataClassNormDto {
    private static final long serialVersionUID = 1L;

    private String id;

    private String dirNorm;

    private Boolean isForecast;

    private String property;

    private Integer variable;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
