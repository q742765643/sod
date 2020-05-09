package com.piesat.schedule.entity.sync;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/17 11:31
 */
@Data
@Entity
@Table(name = "T_SOD_JOB_SYNCCONFIG_INFO")
public class SyncConfigEntity {


     @Id
     @Column(name = "id", unique = true, nullable = false)
     @GeneratedValue(strategy=GenerationType.IDENTITY)
     private Integer id;
    /**
     * 唯一约束键
     * 表中标示唯一约束的键，可以是主键或唯一索引（逗号分隔）
     */
    @Column(name="unique_keys", length = 100)
    private String uniqueKeys;

    /**
     * 分库分表设置
     * 是否有分库分表设置，0表示无，1表示有
     */
    @Column(name="ifpatitions", length = 1)
    private String ifpatitions;

    /**
     * 分库分表键
     * 分库分表键（逗号分隔）
     */
    @Column(name="partition_keys", length = 100)
    private String partitionKeys;

    /**
     * 键值表
     * 是否属于键值表，0表示否，1表示是
     */
    @Column(name="is_kv", length = 1)
    private String isKv;

    /**
     * 存储管理定义的四级编码，与表名一一对应
     */
    @Column(name="d_data_id", length = 20)
    private String dDataId;

    /**
     * 目标表id
     */
    @Column(name="target_table_id", length = 50)
    private String targetTableId;
}
