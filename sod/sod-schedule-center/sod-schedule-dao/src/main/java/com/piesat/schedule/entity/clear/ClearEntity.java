package com.piesat.schedule.entity.clear;

import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.schedule.entity.JobInfoEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 09:38
 **/
@Data
@Entity
@Table(name="T_SOD_JOB_ClEAR_INFO")
@DiscriminatorValue("CLEAR")
public class ClearEntity extends JobInfoEntity{
    @Column(name="database_id", length=50)
    private String databaseId;
    @Column(name="data_class_id", length=50)
    private String dataClassId;
    @Column(name="profile_name", length=255)
    private String profileName;
    @Column(name="d_data_id", length=50)
    private String ddataId;
    @Column(name="table_name", length=50)
    private String tableName;
    @Column(name="conditions", length=255)
    private String conditions;
    @Column(name="clear_limit", length=50)
    private long clearLimit;
    @Column(name="v_table_name", length=50)
    private String vTableName;
    @Column(name="database_type", length=50)
    private String databaseType;
    @Column(name="foreign_key", length=1000)
    private String foreignKey;
    @Column(name="parent_id", length=50)
    private String parentId;
}

