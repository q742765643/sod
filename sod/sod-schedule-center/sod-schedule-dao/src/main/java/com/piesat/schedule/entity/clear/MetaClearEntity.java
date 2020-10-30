package com.piesat.schedule.entity.clear;

import com.piesat.common.annotation.Excel;
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
 * @create: 2020-03-09 10:31
 **/
@Data
@Entity
@Table(name="T_SOD_JOB_META_ClEAR_INFO")
@DiscriminatorValue("METACLEAR")
public class MetaClearEntity extends JobInfoEntity{
    @Excel(name = "物理库ID")
    @Column(name="database_id", length=50)
    private String databaseId;
    @Excel(name = "清除表")
    @Column(name="clear_content",columnDefinition = "TEXT")
    private String clearContent;
    @Excel(name = "物理库基础ID")
    @Column(name="parent_id", length=50)
    private String parentId;
    @Excel(name = "物理库名称")
    @Column(name="database_name", length=200)
    private String databaseName;
    @Excel(name = "where条件")
    @Column(name="conditions", length=200)
    private String conditions;
    @Excel(name = "物理库类型")
    @Column(name="database_type", length=50)
    private String databaseType;

    @Excel(name = "是否删除分区")
    @Column(name="parti_off", length = 10)
    private String partiOff;
}

