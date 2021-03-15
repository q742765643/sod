package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据标签定义
 *
 * @author cwh
 * @date 2020年 07月29日 10:59:09
 */
@Data
@Table(name = "T_SOD_DATACLASS_LABEL_DEF")
@Entity
public class DataClassLabelDefEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "label_name", length = 255, nullable = false)
    private String labelName;

    @Column(name = "status")
    private Integer status;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

}
