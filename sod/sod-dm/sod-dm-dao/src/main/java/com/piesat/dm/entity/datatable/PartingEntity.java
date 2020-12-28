package com.piesat.dm.entity.datatable;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.awt.*;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月16日 15:34:12
 */
@Data
@Table(name = "T_SOD_DATA_TABLE_PARTING")
@Entity
public class PartingEntity extends BaseEntity {

    /**
     * 分库键
     */
    @Column(name = "part_database")
    private String partDatabase;

    /**
     * 分表键
     */
    @Column(name = "part_table")
    private String partTable;

    /**
     * 分区键
     */
    @Column(name = "partitions")
    private String partitions;

    /**
     * 分区维度
     */
    @Column(name = "part_dimension")
    private Integer partDimension;

    /**
     * 分区单位
     */
    @Column(name = "part_unit")
    private String partUnit;
}
