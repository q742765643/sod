package com.piesat.schedule.entity.recover;

import com.piesat.common.annotation.Excel;
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
    @Excel(name = "物理库ID")
    @Column(name="database_id", length=50)
    private String databaseId;
    @Excel(name = "物理库类型")
    @Column(name="database_type", length=50)
    private String databaseType;
    @Excel(name = "备份类型",readConverterExp = "0=结构,1=数据,0,1=结构+数据")
    @Column(name="is_structure", length=50)
    private String isStructure;
    @Column(name="recover_content",columnDefinition = "TEXT")
    private String recoverContent;
    @Excel(name = "物理库基础ID")
    @Column(name="parent_id", length=50)
    private String parentId;
    @Excel(name = "物理库名称")
    @Column(name="datbase_name", length=200)
    private String databaseName;
    @Excel(name = "目录")
    @Column(name="storage_directory", length=200)
    private String storageDirectory;

}

