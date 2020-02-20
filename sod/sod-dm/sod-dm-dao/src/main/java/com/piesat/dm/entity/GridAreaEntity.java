package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:17:36
 */
@Data
@Table(name = "T_SOD_GRID_AREA")
@Entity
public class GridAreaEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "data_service_id", length = 255)
    private String dataServiceId;


    @Column(name = "area_id", length = 255)
    private String areaId;

    @Column(name = "area_region_desc", length = 255)
    private String areaRegionDesc;

}
