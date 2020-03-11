package com.piesat.schedule.entity.backup;

import com.piesat.common.annotation.Excel;
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
    @Excel(name = "备份时次", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    @Column(name="backup_time")
    private long backupTime;
    @Excel(name = "物理库类型")
    @Column(name="database_type", length=50)
    private String databaseType;
    @Excel(name = "物理库基础ID")
    @Column(name="parent_id", length=50)
    private String parentId;
    @Excel(name = "备份文件名")
    @Column(name="file_name", length=200)
    private String fileName;
    @Column(name="foreign_key", length=1000)
    private String foreignKey;
    @Column(name="is_end", length=2)
    private Integer isEnd;
}

