package com.piesat.dm.entity.database;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Table(name = "T_SOD_DATABASE_AUTHORIZED")
@Entity
public class DatabaseAuthorizedEntity extends BaseEntity {
    @Column(name = "database_username")
    private String databaseUsername;

    @Column(name = "database_id")
    private String databaseId;

    @Column(name = "status", columnDefinition = "Boolean default false")
    private Boolean status;

    @Column(name = "ope_type")
    private String opeType;

    @Column(name = "msg", columnDefinition = "TEXT")
    private String msg;
}
