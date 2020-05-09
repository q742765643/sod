package com.piesat.schedule.entity.sync;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/17 10:05
 */
@Data
@Entity
@Table(name = "T_SOD_JOB_SYNCFILTER_INFO")
public class SyncFilterEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    /**
     * 过滤字段列名
     */
    @Column(name="column_name", length = 50)
    private String columnName;

    /**
     * 列名对应的过滤值
     */
    @Column(name="filter_values", length = 50)
    private String filterValues;

    /**
     * 列的操作符
     */
    @Column(name="column_oper", length = 10)
    private String columnOper;
}
