package com.piesat.schedule.entity.move;

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
 * @create: 2019-12-24 15:19
 **/
@Data
@Entity
@Table(name="T_SOD_JOB_MOVE_INFO")
@DiscriminatorValue("MOVE")
public class MoveEntity extends JobInfoEntity {
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
    @Excel(name = "迁移条件")
    @Column(name="conditions", length=255)
    private String conditions;
    @Excel(name = "清除条件")
    @Column(name="clear_conditions", length=255)
    private String clearConditions;
    @Column(name="archive_conditions", length=255)
    private String archiveConditions;
    @Excel(name = "源目录")
    @Column(name="source_directory", length=255)
    private String sourceDirectory;
    @Excel(name = "目标目录")
    @Column(name="target_directory", length=255)
    private String targetDirectory;
    @Column(name="is_clear", length=1)
    private String isClear;
    @Excel(name = "迁移频率")
    @Column(name="move_limit", length=50)
    private long moveLimit;
    @Excel(name = "要素表名")
    @Column(name="v_table_name", length=50)
    private String vTableName;
    @Excel(name = "物理库类型")
    @Column(name="database_type", length=50)
    private String databaseType;
    @Column(name="foreign_key", length=1000)
    private String foreignKey;
    @Excel(name = "物理库基础ID")
    @Column(name="parent_id", length=50)
    private String parentId;
    @Column(name="primary_key", length=50)
    private String primaryKey;
}

