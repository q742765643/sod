package com.piesat.schedule.entity.sync;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/17 16:37
 */
@Data
@Entity
@Table(name = "T_SOD_JOB_SYNCMAPPING_INFO")
public class SyncMappingEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    /**
     * 对应filter里的record_id
     */
    @Column(name="source_table_id", length = 50)
    private String sourceTableId;

    /**
     * 源数据表名
     */
    @Column(name="source_table_name", length = 50)
    private String sourceTableName;

    /**
     * 对应config里的record_id
     */
    @Column(name="target_table_id", length = 50)
    private String targetTableId;

    /**
     * 目标表名
     */
    @Column(name="target_table_name", length = 50)
    private String targetTableName;

    /**
     * 列映射关系
     */
    @Column(name="mapping", columnDefinition="TEXT",nullable=true)
    private String mapping;
}
