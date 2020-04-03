package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资料用途分类
 *
 * @author cwh
 * @date 2019年 11月20日 17:04:05
 */
@Data
@Table(name = "T_SOD_DATA_LOGIC")
@Entity
public class DataLogicEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * data_class_id
     */
    @Column(name = "data_class_id", length = 255, nullable = false)
    private String dataClassId;

    /**
     * logic_id
     */
    @Column(name = "logic_flag", length = 255, nullable = false)
    private String logicFlag;

    /**
     * storage_type
     */
    @Column(name = "storage_type", length = 255, nullable = false)
    private String storageType;

    /**
     * database_type
     */
    @Column(name = "database_id", length = 255, nullable = false)
    private String databaseId;

    @Column(name = "is_complete", columnDefinition = "Boolean default false ")
    private Boolean isComplete;
}
