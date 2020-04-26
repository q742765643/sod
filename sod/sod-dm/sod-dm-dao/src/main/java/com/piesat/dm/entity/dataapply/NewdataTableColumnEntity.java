package com.piesat.dm.entity.dataapply;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yaya
 * @description 新增资料申请时带过来的字段信息
 * @date 2020/4/18 9:38
 */
@Data
@Entity
@Table(name = "T_SOD_NEWDATA_TABLE_COLUMN")
public class NewdataTableColumnEntity extends BaseEntity {

    /**
     * 申请记录id
     */
    @Column(name = "apply_id", length = 36)
    private String applyId;

    /**
     * 字段名称
     * c_element_code
     */
    @Column(name = "c_element_code", length = 36)
    private String cElementCode;

    /**
     * 中文名称
     * ele_name
     */
    @Column(name = "ele_name", length = 255)
    private String eleName;

    /**
     * 字段类型
     * type
     */
    @Column(name = "type", length = 36)
    private String type;

    /**
     * 字段精度
     * accuracy
     */
    @Column(name = "accuracy", length = 36)
    private String accuracy;

    /**
     * 英文单位
     * unit
     */
    @Column(name = "unit", length = 36)
    private String unit;

    /**
     * 是否可为空
     * is_null
     */
    @Column(name = "is_null", columnDefinition = "Boolean")
    private Boolean isNull;

    /**
     * 是否主键
     * is_primary_key
     */
    @Column(name = "is_primary_key", columnDefinition = "Boolean")
    private Boolean isPrimaryKey;

    /**
     * 序号
     * serial_number
     */
    @Column(name = "serial_number")
    private Integer serialNumber;


}
