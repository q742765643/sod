package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据标签
 *
 * @author cwh
 * @date 2020年 07月29日 10:59:09
 */
@Data
@Table(name = "T_SOD_DATA_CLASS_LABEL")
@Entity
public class DataClassLabelEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "data_class_id", length = 255, nullable = false)
    private String dataClassId;

    @Column(name = "label_key", length = 255, nullable = false)
    private String labelKey;

}
