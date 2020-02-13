package com.piesat.dm.rpc.dto;

import lombok.Data;

import java.util.Date;

/**
 * 表数据统计
 *
 * @author cwh
 * @date 2020年 02月13日 14:44:37
 */
@Data
public class TableDataStatisticsDto {
    private static final long serialVersionUID = 1L;

    private String id;

    private String databaseId;

    private String tableId;

    private Date statisticDate;

    private Date beginTime;

    private Date endTime;

    private long recordCount;

    private int dayTotal;

    private String statisticTime;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
