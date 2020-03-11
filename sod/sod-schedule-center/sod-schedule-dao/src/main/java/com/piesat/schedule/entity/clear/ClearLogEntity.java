package com.piesat.schedule.entity.clear;

import com.piesat.common.annotation.Excel;
import com.piesat.schedule.entity.JobInfoEntity;
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
 * @create: 2019-12-27 18:16
 **/
@Data
@Entity
@Table(name="T_SOD_JOB_CLEAR_INFO_LOG")
@DiscriminatorValue("CLEAR")
public class ClearLogEntity extends JobInfoLogEntity {
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
    @Excel(name = "键表名")
    @Column(name="table_name", length=50)
    private String tableName;
    @Excel(name = "清除条件")
    @Column(name="conditions", length=255)
    private String conditions;
    @Excel(name = "清除频率")
    @Column(name="clear_limit", length=50)
    private long clearLimit;
    @Excel(name = "要素表名")
    @Column(name="v_table_name", length=50)
    private String vTableName;
    @Column(name="clear_count", length=50)
    private long clearCount;
    @Column(name="foreign_key", length=1000)
    private String foreignKey;
    @Excel(name = "物理库类型")
    @Column(name="database_type", length=50)
    private String databaseType;
    @Excel(name = "物理库基础ID")
    @Column(name="parent_id", length=50)
    private String parentId;
    @Column(name="clear_time", length=50)
    private long clearTime;

}

