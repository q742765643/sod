package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据用途与物理库关系
 *
 * @author cwh
 * @date 2020年 02月10日 15:31:20
 */
@Data
@Table(name = "T_SOD_LOGIC_DATABASE")
@Entity
public class LogicDatabaseEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "logic_id")
    private String logicId;

    @Column(name = "database_id", length = 255, nullable = false)
    private String databaseId;

}
