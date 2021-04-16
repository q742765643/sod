package com.piesat.dm.entity.dataclass;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author cwh
 * @version 1.0.0
 * @ClassName DataClassServiceCodeEntity.java
 * @Description TODO
 * @createTime 2021年04月15日 14:22:00
 */
@Data
@Table(name = "T_SOD_DATACLASS_SERVICE_CODE")
@Entity
public class DataClassServiceCodeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 存储编码
     * data_class_id
     */
    @Column(name = "data_class_id", length = 255, nullable = false)
    private String dataClassId;

    /**
     * 字段id
     */
    @Column(name = "table_column_id", length = 255, nullable = false)
    private String TableColumnId;

    /**
     * 服务名称
     * user_ele_code
     */
    @Column(name = "user_ele_code", length = 36)
    private String userEleCode;

    /**
     * 英文单位
     * unit
     */
    @Column(name = "unit", length = 136)
    private String unit;

    /**
     * 中文单位
     * unit_cn
     */
    @Column(name = "unit_cn", length = 36)
    private String unitCn;

    /**
     * 是否可显示
     * is_show
     */
    @Column(name = "is_show")
    private Integer isShow;


    /**
     * 是否管理字段
     * is_manager
     */
    @Column(name = "is_manager")
    private Integer isManager;


}
