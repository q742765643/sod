package com.piesat.schedule.entity.backup;

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
 * @create: 2020-02-26 14:34
 **/
@Data
@Entity
@Table(name="T_SOD_JOB_METABACKUP_INFO")
@DiscriminatorValue("METABACKUP")
public class MetaBackupEntity extends JobInfoEntity {
    @Column(name="database_id", length=50)
    private String databaseId;
    @Column(name="is_structure", length=50)
    private String isStructure;
    @Column(name="back_content", length=5000)
    private String backContent;
    @Column(name="parent_id", length=50)
    private String parentId;
    @Column(name="datbase_name", length=200)
    private String databaseName;

}

