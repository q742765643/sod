package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 数据用途与存储类型对应关系
 *
 * @author cwh
 * @date 2019年 11月21日 17:11:14
 */
@Data
@Table(name = "T_SOD_LOGIC_STORAGE")
@Entity
public class LogicStorageTypesEntity extends BaseEntity {

    @Column(name = "logic_id")
    private String logicId;

    @Column(name = "storage_type", length = 255, nullable = false)
    private String storageType;

}
