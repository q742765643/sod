package com.piesat.schedule.entity.backup;

import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.JobInfoLogEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 15:00
 **/
@Data
@Entity
@Table(name="T_SOD_JOB_BACKUP_INFO_LOG")
@DiscriminatorValue("BACKUP")
public class BackupLogEntity extends JobInfoLogEntity{
    @Column(name="database_id", length=50)
    private String databaseId;
    @Column(name="data_class_id", length=50)
    private String dataClassId;
    @Column(name="profile_name", length=255)
    private String profileName;
    @Column(name="d_data_id", length=50)
    private String ddataId;
    @Column(name="conditions", length=255)
    private String conditions;
    @Column(name="second_conditions", length=255)
    private String secondConditions;
    @Column(name="storage_directory", length=255)
    private String storageDirectory;
    @Column(name="table_name", length=50)
    private String tableName;
    @Column(name="v_table_name", length=50)
    private String vTableName;
    @Column(name="backup_time")
    private long backupTime;
    @Column(name="database_type", length=50)
    private String databaseType;
    @Column(name="parent_id", length=50)
    private String parentId;
    @Column(name="file_name", length=200)
    private String fileName;
    @Column(name="foreign_key", length=50)
    private String foreignKey;
    @Column(name="is_end", length=2)
    private Integer isEnd;
}

