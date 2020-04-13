package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 一致性检查
 * @author yaya
 * @description TODO
 * @date 2020/2/14 10:26
 */
@Data
@Table(name = "T_SOD_CONSISTENCY_CHECK")
@Entity
public class ConsistencyCheckEntity extends BaseEntity {

    /**
     * 与数据库关联id
     * database_id
     */
    @Column(name = "database_id",length = 50)
    private String databaseId;

    /**
     * 数据库名称
     */
    @Column( name = "database_name", length = 50)
    private String databaseName;
}
