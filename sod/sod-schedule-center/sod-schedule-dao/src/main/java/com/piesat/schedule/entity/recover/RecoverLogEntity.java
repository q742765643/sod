package com.piesat.schedule.entity.recover;

import com.piesat.common.annotation.Excel;
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
    @Excel(name = "物理库ID")
    @Column(name="database_id", length=50)
    private String databaseId;
    @Excel(name = "存储编码")
    @Column(name="data_class_id", length=50)
    private String dataClassId;
    @Excel(name = "资料名称")
    @Column(name="profile_name", length=255)
    private String profileName;
    @Excel(name = "四级编码")
    @Column(name="d_data_id", length=50)
    private String ddataId;
    @Excel(name = "存储目录")
    @Column(name="storage_directory", columnDefinition = "TEXT")
    private String storageDirectory;
    @Excel(name = "键表")
    @Column(name="table_name", length=50)
    private String tableName;
    @Excel(name = "要素表")
    @Column(name="v_table_name", length=50)
    private String vTableName;
    @Excel(name = "物理库类型")
    @Column(name="database_type", length=50)
    private String databaseType;
    @Excel(name = "物理库基础ID")
    @Column(name="parent_id", length=50)
    private String parentId;
}

