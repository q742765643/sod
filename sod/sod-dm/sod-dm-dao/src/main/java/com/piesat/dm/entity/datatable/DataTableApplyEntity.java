package com.piesat.dm.entity.datatable;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author cwh
 * @program: sod
 * @description: 表申请
 * @date 2021年 03月10日 14:19:57
 */
@Data
@Table(name = "T_SOD_DATA_TABLE_APPLY")
@Entity
public class DataTableApplyEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库id
     */
    @Column(name = "database_id", length = 255)
    private String databaseId;

    /**
     * 表存储类型
     */
    @Column(name = "storage_type", length = 255)
    private String storageType;

    /**
     * 表名
     */
    @Column(name = "table_name", length = 255)
    private String tableName;

    /**
     * 表子类型（键表、要素表）
     */
    @Column(name = "table_type", length = 36)
    private String tableType;

    /**
     * 表描述
     */
    @Column(name = "table_desc", length = 255)
    private String tableDesc;

    /**
     * 中文名
     */
    @Column(name = "name_cn", length = 255)
    private String nameCn;

    /**
     * 创建人
     */
    @Column(name = "creator", length = 255)
    private String creator;

    /**
     * 申请人
     */
    @Column(name = "user_id", length = 255)
    private String userId;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 描述
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="table_id")
    @OrderBy("serialNumber ASC")
    private Set<TableColumnEntity> columns = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="table_id")
    private Set<TableIndexEntity> tableIndexList = new LinkedHashSet<>();
}
