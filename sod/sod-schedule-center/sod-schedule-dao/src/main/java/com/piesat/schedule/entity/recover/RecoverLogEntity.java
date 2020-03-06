package com.piesat.schedule.entity.recover;

import com.piesat.schedule.entity.JobInfoLogEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-05 11:25
 **/
@Data
@Entity
@Table(name="T_SOD_RECOVER_LOG")
@DiscriminatorValue("RECOVER")
public class RecoverLogEntity extends JobInfoLogEntity{
    @Column(name="database_id", length=50)
    private String databaseId;
    @Column(name="data_class_id", length=50)
    private String dataClassId;
    @Column(name="profile_name", length=255)
    private String profileName;
    @Column(name="d_data_id", length=50)
    private String ddataId;
    @Column(name="storage_directory", length=255)
    private String storageDirectory;
    @Column(name="table_name", length=50)
    private String tableName;
    @Column(name="v_table_name", length=50)
    private String vTableName;
    @Column(name="database_type", length=50)
    private String databaseType;
    @Column(name="parent_id", length=50)
    private String parentId;
}

