package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月16日 15:52:04
 */
@Data
public class PartingDto {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 分库键
     */
    private String partDatabase;

    /**
     * 分表键
     */
    private String partTable;

    /**
     * 分区键
     */
    private String partitions;

    /**
     * 分区维度
     */
    private Integer partDimension;

    /**
     * 分区单位
     */
    private String partUnit;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
