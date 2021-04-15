package com.piesat.dm.entity.datatable;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月20日 17:06:14
 */
@Data
@Table(name = "T_SOD_DATA_TABLE_COLUMNS")
@Entity
public class TableColumnEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * table_id
     */
    @Column(name = "table_id", length = 255)
    private String tableId;

    /**
     * 列名称
     * db_ele_code
     */
    @Column(name = "db_ele_code", length = 36, nullable = false)
    private String dbEleCode;


    /**
     * 中文描述
     * name_cn
     */
    @Column(name = "name_cn", length = 255)
    private String nameCn;

    /**
     * 中文名称
     * ele_name
     */
    @Column(name = "ele_name", length = 255)
    private String eleName;

    /**
     *
     * type
     */
    @Column(name = "type", length = 36, nullable = false)
    private String type;

    /**
     * 字段类型
     * length
     */
    @Column(name = "length")
    private Integer length;

    /**
     * 字段精度
     * accuracy
     */
    @Column(name = "accuracy", length = 36)
    private String accuracy;

    /**
     * 是否可为空
     * is_null
     */
    @Column(name = "is_null", columnDefinition = "Boolean")
    private Boolean isNull;

    /**
     *是否可更新
     * is_update
     */
    @Column(name = "is_update", columnDefinition = "Boolean")
    private Boolean isUpdate;

    /**
     * 是否是KV库的键字段
     * is_kv_k
     */
    @Column(name = "is_kv_k", columnDefinition = "Boolean")
    private Boolean isKvK;

    /**
     * 序号
     * serial_number
     */
    @Column(name = "serial_number")
    private Integer serialNumber;

    /**
     * 序号
     * is_primary_key
     */
    @Column(name = "is_primary_key", columnDefinition = "Boolean")
    private Boolean isPrimaryKey;
    /**
     * 默认值
     * default_value
     */
    @Column(name = "default_value", length = 255)
    private String defaultValue;

}
