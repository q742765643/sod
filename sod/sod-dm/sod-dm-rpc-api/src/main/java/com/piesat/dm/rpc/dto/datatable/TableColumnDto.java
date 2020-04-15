package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import java.util.Date;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月20日 17:06:14
 */
@Data
public class TableColumnDto {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * table_id
     */
    private String tableId;

    /**
     * db_ele_code
     */
    private String dbEleCode;

    /**
     * c_element_code
     */
    private String cElementCode;

    /**
     * is_manager
     */
    private Boolean isManager;

    /**
     * user_ele_code
     */
    private String userEleCode;

    /**
     * name_cn
     */
    private String nameCn;

    /**
     * ele_name
     */
    private String eleName;

    /**
     * type
     */
    private String type;

    /**
     * length
     */
    private Integer length;

    /**
     * accuracy
     */
    private String accuracy;

    /**
     * unit
     */
    private String unit;

    /**
     * unit_cn
     */
    private String unitCn;

    /**
     * is_null
     */
    private Boolean isNull;

    /**
     * is_update
     */
    private Boolean isUpdate;

    /**
     * is_show
     */
    private Boolean isShow;

    /**
     * is_kv_k
     */
    private Boolean isKvK;

    /**
     * database_type
     */
    private String databaseType;

    private Boolean updateDatabase;

    /**
     * serial_number
     */
    private Integer serialNumber;

    private Boolean isPrimaryKey;

    /**
     * default_value
     */
    private String defaultValue;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
