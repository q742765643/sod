package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月20日 17:06:14
 */
@Data
@Table(name = "T_SOD_DATA_TABLE_COLUMN")
@Entity
public class TableColumnEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * table_id
     */
    @ManyToOne(targetEntity = DataTableEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id")
    private DataTableEntity tableId;

    /**
     * db_ele_code
     */
    @Column(name = "db_ele_code", length = 36, nullable = false)
    private String dbEleCode;

    /**
     * c_element_code
     */
    @Column(name = "c_element_code", length = 36, nullable = false)
    private String cElementCode;

    /**
     * is_manager
     */
    @Column(name = "is_manager", columnDefinition = "Boolean")
    private Boolean isManager;

    /**
     * user_ele_code
     */
    @Column(name = "user_ele_code", length = 36, nullable = false)
    private String userEleCode;

    /**
     * name_cn
     */
    @Column(name = "name_cn", length = 255, nullable = false)
    private String nameCn;

    /**
     * ele_name
     */
    @Column(name = "ele_name", length = 255)
    private String eleName;

    /**
     * type
     */
    @Column(name = "type", length = 36, nullable = false)
    private String type;

    /**
     * length
     */
    @Column(name = "length")
    private Integer length;

    /**
     * accuracy
     */
    @Column(name = "accuracy", length = 36)
    private String accuracy;

    /**
     * unit
     */
    @Column(name = "unit", length = 36)
    private String unit;

    /**
     * unit_cn
     */
    @Column(name = "unit_cn", length = 36)
    private String unitCn;

    /**
     * is_null
     */
    @Column(name = "is_null", columnDefinition = "Boolean")
    private Boolean isNull;

    /**
     * is_update
     */
    @Column(name = "is_update")
    private Date isUpdate;

    /**
     * is_show
     */
    @Column(name = "is_show", columnDefinition = "Boolean")
    private Boolean isShow;

    /**
     * is_kv_k
     */
    @Column(name = "is_kv_k", columnDefinition = "Boolean")
    private Boolean isKvK;

    /**
     * database_type
     */
    @Column(name = "database_type", length = 36)
    private String databaseType;

    /**
     * serial_number
     */
    @Column(name = "serial_number")
    private Integer serialNumber;

    /**
     * default_value
     */
    @Column(name = "default_value", length = 255)
    private String defaultValue;

}
