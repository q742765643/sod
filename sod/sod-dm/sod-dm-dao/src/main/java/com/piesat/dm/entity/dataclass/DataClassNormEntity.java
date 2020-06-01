package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 规范信息
 *
 * @author cwh
 * @date 2020年 02月12日 16:45:56
 */
@Data
@Table(name = "T_SOD_DATACLASS_NORM")
@Entity
public class DataClassNormEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "dir_norm", length = 855)
    private String dirNorm;

    @Column(name = "is_forecast", columnDefinition = "Boolean default false ")
    private Boolean isForecast;

    @Column(name = "property", length = 255)
    private String property;

    @Column(name = "variable", length = 255)
    private Integer variable;
}
