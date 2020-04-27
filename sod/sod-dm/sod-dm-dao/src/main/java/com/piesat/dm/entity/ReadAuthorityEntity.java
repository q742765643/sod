package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/27 10:38
 */
@Data
@Table(name = "T_SOD_READ_AUTHORITY")
@Entity
public class ReadAuthorityEntity extends BaseEntity {

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "value", length = 1)
    private String value;

    @Column(name = "description", length = 50)
    private String description;
}
