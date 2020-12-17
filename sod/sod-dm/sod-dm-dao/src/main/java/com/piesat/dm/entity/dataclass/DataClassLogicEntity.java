package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资料用途分类
 *
 * @author cwh
 * @date 2019年 11月20日 17:04:05
 */
@Data
@Table(name = "T_SOD_DATACLASS_TABLE")
@Entity
public class DataClassLogicEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 存储编码
     */
    @Column(name = "data_class_id", length = 255, nullable = false)
    private String dataClassId;

    /**
     * 表id
     */
    @Column(name = "table_id", length = 255)
    private String tableId;

    /**
     * 子表id
     */
    @Column(name = "sub_table_id", length = 255)
    private String subTableId;
}
