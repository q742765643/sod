package com.piesat.schedule.entity.clear;

import com.piesat.common.annotation.Excel;
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
    @Excel(name = "数据类型")
    @Column(name="database_type", length=50)
    private String databaseType;
    @Column(name="foreign_key", length=1000)
    private String foreignKey;
    @Excel(name = "物理库基础ID")
    @Column(name="parent_id", length=50)
    private String parentId;
}

