package com.piesat.dm.entity.database;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据库用户审核日志
 * @author cwh
 * @date 2020年 12月16日 14:25:34
 */
@Data
@Table(name = "T_SOD_DBUSER_ALTER_LOG")
@Entity
public class DbUserAlterLogEntity extends BaseEntity {

    @Column(name = "authorize_id")
    private String authorizeId;

    @Column(name = "log", columnDefinition = "TEXT", nullable = true)
    private String log;

    @Column(name = "ope_type")
    private String opeType;

    @Column(name = "status", columnDefinition = "Boolean default false")
    private Boolean status;
}
