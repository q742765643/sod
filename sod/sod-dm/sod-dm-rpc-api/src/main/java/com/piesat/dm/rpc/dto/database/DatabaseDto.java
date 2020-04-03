package com.piesat.dm.rpc.dto.database;

import com.piesat.dm.rpc.dto.special.DatabaseSpecialAuthorityDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 数据库基础库专题库
 *
 * @author cwh
 * @date 2019年 11月21日 18:03:14
 */
@Data
public class DatabaseDto {
    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 数据库名称
     * database_name
     */
    private String databaseName;

    /**
     * 与数据库定义关联id
     * database_id
     */
    private DatabaseDefineDto databaseDefine;

    /**
     * 数据库模式
     * schema_name
     */
    private String schemaName;

    /**
     * 数据库分类（物理库，专题库）
     * database_classify
     */
    private String databaseClassify;

    /**
     * 与申请表关联id
     * tdb_id
     */
    private String tdbId;

    private Boolean stopUse;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;

    /**
     * 用户ID
     */
    private String userId;

    private Integer level;

    private List<DatabaseSpecialAuthorityDto> databaseSpecialAuthorityList;
}
