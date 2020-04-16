package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/16 10:57
 */
@Data
@Table(name = "T_SOD_CONSISTENCY_CHECK_HISTORY")
@Entity
public class ConsistencyCheckHistoryEntity extends BaseEntity {

    /**
     * 与数据库关联id
     * database_id
     */
    @Column(name = "database_id",length = 50)
    private String databaseId;

    /**
     * 文件路径
     */
    @Column(name = "file_directory",length = 500)
    private String fileDirectory;

    /**
     * 文件名称
     */
    @Column(name = "file_name",length = 100)
    private String fileName;

}
