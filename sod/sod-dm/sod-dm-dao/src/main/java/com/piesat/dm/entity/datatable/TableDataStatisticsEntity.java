package com.piesat.dm.entity.datatable;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 表数据统计
 *
 * @author cwh
 * @date 2020年 02月13日 14:34:44
 */
@Data
@Table(name = "T_SOD_TABLEDATA_STATISTICS")
@Entity
public class TableDataStatisticsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "database_id", length = 255)
    private String databaseId;

    @Column(name = "table_id", length = 255)
    private String tableId;

    @Column(name = "statistic_date", length = 255)
    private Date statisticDate;

    @Column(name = "begin_time", length = 255)
    private Date beginTime;

    @Column(name = "end_time", length = 255)
    private Date endTime;

    @Column(name = "record_count", length = 255)
    private Double recordCount;

    @Column(name = "day_total", length = 255)
    private int dayTotal;

    @Column(name = "statistic_time", length = 255)
    private String statisticTime;
}
