package com.piesat.schedule.entity.backup;

import com.piesat.common.annotation.Excel;
import com.piesat.schedule.entity.JobInfoEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 15:14
 **/
@Data
@Entity
@Table(name="T_SOD_JOB_BACKUP_INFO")
@DiscriminatorValue("BACKUP")
public class BackupEntity extends JobInfoEntity{
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
    @Excel(name = "近时备份条件")
    @Column(name="conditions", length=255)
    private String conditions;
    @Excel(name = "远时备份条件")
    @Column(name="second_conditions", length=255)
    private String secondConditions;
    @Excel(name = "存储目录")
    @Column(name="storage_directory", length=255)
    private String storageDirectory;
    @Excel(name = "键表名")
    @Column(name="table_name", length=50)
    private String tableName;
    @Excel(name = "要素表名")
    @Column(name="v_table_name", length=50)
    private String vTableName;
    @Excel(name = "物理库类型")
    @Column(name="database_type", length=50)
    private String databaseType;
    @Excel(name = "物理库基础ID")
    @Column(name="parent_id", length=50)
    private String parentId;
    @Column(name="foreign_key", length=1000)
    private String foreignKey;
    @Column(name="backup_start_time", length=50)
    private Date  backupStartTime;


}

