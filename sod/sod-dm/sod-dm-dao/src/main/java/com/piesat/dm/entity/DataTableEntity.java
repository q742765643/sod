package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月20日 17:04:53
 */
@Data
@Table(name = "T_SOD_DATA_TABLE")
@Entity
public class DataTableEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * class_logic_id
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "class_logic_id")
    private DataLogicEntity classLogicId;

    /**
     * data_service_id
     */
    @Column(name = "data_service_id", length = 255)
    private String dataServiceId;


    /**
     * table_name
     */
    @Column(name = "table_name", length = 255)
    private String tableName;


    /**
     * db_table_type
     */
    @Column(name = "db_table_type", length = 36)
    private String dbTableType;

    /**
     * table_desc
     */
    @Column(name = "table_desc", length = 255)
    private String tableDesc;

    /**
     * data_service_name
     */
    @Column(name = "data_service_name", length = 255)
    private String dataServiceName;

    /**
     * name_cn
     */
    @Column(name = "name_cn", length = 255)
    private String nameCn;

    /**
     * creator
     */
    @Column(name = "creator", length = 255)
    private String creator;

    /**
     * user_id
     */
    @Column(name = "user_id", length = 255)
    private String userId;

    @OneToMany(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name="table_id")
    private Set<TableColumnEntity> columns = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name="table_id")
    private Set<TableIndexEntity> tableIndexList = new HashSet<>();
}
