package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资料用户权限
 *
 * @author cwh
 * @date 2020年 07月29日 10:59:09
 */
@Data
@Table(name = "T_SOD_DATA_CLASS_USER")
@Entity
public class DataClassUserEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "data_class_id", length = 255, nullable = false)
    private String dataClassId;

    @Column(name = "user_name", length = 255, nullable = false)
    private String userName;

}
