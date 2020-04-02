package com.piesat.dm.entity.special;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 专题库管理
 *
 * @author wulei
 * @date 2020年 2月12日 15:12:47
 */
@Data
@Table(name = "T_SOD_DATABASE_SPECIAL_AUTHORITY")
@Entity
//@Proxy(lazy = false)
public class DatabaseSpecialAuthorityEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 专题库ID
     */
    @Column(name = "sdb_id")
    private String sdbId;

    /**
     * 物理课ID
     */
    @Column(name = "database_id")
    private String databaseId;

    /**
     * 建表权限
     */
    @Column(name = "create_table")
    private Integer createTable;

    /**
     * 删表权限
     */
    @Column(name = "delete_table")
    private Integer deleteTable;

    /**
     * 表数据访问权限
     */
    @Column(name = "table_data_access")
    private Integer tableDataAccess;

}
