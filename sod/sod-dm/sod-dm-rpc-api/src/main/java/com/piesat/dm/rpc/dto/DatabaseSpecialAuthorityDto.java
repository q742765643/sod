package com.piesat.dm.rpc.dto;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 专题库权限
 *
 * @author wulei
 * @date 2020年 2月12日 15:12:47
 */
@Data
public class DatabaseSpecialAuthorityDto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 专题库ID
     */
    private String sdbId;

    /**
     * 物理课ID
     */
    private String databaseId;

    /**
     * 建表权限
     */
    private String createTable;

    /**
     * 删表权限
     */
    private String deleteTable;

    /**
     * 表数据访问权限
     */
    private String tableDataAccess;

}