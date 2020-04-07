package com.piesat.dm.entity.datatable;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 11:50:14
 */
@Data
@Table(name = "T_SOD_DATA_TABLE_FOREIGN_KEY")
@Entity
public class TableForeignKeyEntity extends BaseEntity {

    /* 表id */
    @Column(name = "class_logic_id", length = 255)
    private String classLogicId;

    /* 键字段 */
    @Column(name = "key_column", length = 255)
    private String keyColumn;

    /* 要素字段 */
    @Column(name = "ele_column", length = 255)
    private String eleColumn;

    /* 是否真实存在 */
    @Column(name = "is_real", columnDefinition = "Boolean")
    private Boolean isReal;
}
