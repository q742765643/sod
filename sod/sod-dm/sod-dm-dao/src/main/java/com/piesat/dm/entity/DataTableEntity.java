package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

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
     * table_id
     */
    @Column(name = "table_id", length = 255, nullable = false)
    private String tableId;

    /**
     * class_logic_id
     */
    @Column(name = "class_logic_id", length = 255, nullable = false)
    private String classLogicId;

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

}
