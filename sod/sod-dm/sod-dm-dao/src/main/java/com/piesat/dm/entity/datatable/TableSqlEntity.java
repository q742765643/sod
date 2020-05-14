package com.piesat.dm.entity.datatable;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 表sql
 *
 * @author cwh
 * @date 2020年 04月02日 14:00:34
 */
@Data
@Table(name = "T_SOD_DATA_TABLE_SQL")
@Entity
public class TableSqlEntity extends BaseEntity {

    @Column(name = "database_id", length = 255)
    private String databaseId;

    @Column(name = "table_name", length = 255)
    private String tableName ;

    @Column(name = "table_sql", columnDefinition="TEXT")
    private String tableSql ;
}
