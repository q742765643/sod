package com.piesat.schedule.entity.recover;

import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.schedule.entity.JobInfoLogEntity;
import com.sun.corba.se.spi.activation.BadServerDefinition;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 13:06
 **/
@Data
@Entity
@Table(name="T_SOD_META_RECOVER_LOG")
@DiscriminatorValue("METARECOVER")
public class MetaRecoverLogEntity extends JobInfoLogEntity {
    @Column(name="database_id", length=50)
    private String databaseId;
    @Column(name="database_type", length=50)
    private String databaseType;
    @Column(name="is_structure", length=50)
    private String isStructure;
    @Column(name="recover_content",columnDefinition = "TEXT")
    private String recoverContent;
    @Column(name="parent_id", length=50)
    private String parentId;
    @Column(name="datbase_name", length=200)
    private String databaseName;
    @Column(name="storage_directory", length=200)
    private String storageDirectory;
    @Column(name="task_name", length=200)
    private String taskName;
}

