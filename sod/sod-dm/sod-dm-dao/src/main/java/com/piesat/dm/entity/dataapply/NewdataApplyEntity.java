package com.piesat.dm.entity.dataapply;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/5 15:42
 */
@Data
@Entity
@Table(name = "T_SOD_NEWDATA_APPLY")
public class NewdataApplyEntity{

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "create_time",nullable = false)
    private Date createTime;

    /**
     * 四级编码
     */
    @Column(name = "d_data_id", length = 20)
    private String dDataId;

    /**
     * 服务编码
     * MUSIC接口注册时提供
     */
    @Column(name = "data_service_id", length = 20)
    private String dataServiceId;

   /**
     * 存储编码
    */
    @Column(name = "data_class_id", length = 20)
    private String dataClassId;

   /**
     * 逻辑库ID
    */

    @Column(name = "logic_id", length = 64)
    private String logicId;

    /**
     * 数据库ID
    */
    @Column(name = "database_id", length = 64)
    private String databaseId;

    /**
     * 表英文名称
     */
    @Column(name = "table_name", length = 64)
    private String tableName;

    /**
     * 数据采集频次
     */
    @Column(name = "data_freq", length = 50)
    private String dataFreq;

    /**
     * 常用使用条件字段
     * 辅助管理员建索引，多个字段用逗号分隔
     */
    @Column(name = "freuse_field", length = 200)
    private String freuseField;

    /**
     * 服务权限
     * 1公开,2限制
     */
    @Column(name = "is_publish")
    private Integer isPublish;

    /**
     * 其他说明
     */
    @Column(name = "memo", length = 500)
    private String memo;

    /**
     * 申请用户ID
     */
    @Column(name = "user_id", length = 50)
    private String userId;

    /**
     * 数据来源
     * 包括：MUSIC接口注册、Web用户注册、静态数据注册
     */
    @Column(name = "data_origin", length = 50)
    private String dataOrigin;

    /**
     * 审核人
     */
    @Column(name = "examiner", length = 50)
    private String examiner;

    /**
     * 审核状态
     * 1-待审核 ,2-审核通过 ,3-审核不通过，4-删除申请中，5-已删除
     */
    @Column(name = "examine_status")
    private Integer examineStatus;

    /**
     * 审核时间
     */
    @Column(name = "examine_time")
    private Date examineTime;

    /**
     * 驳回原因
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 存储构建状态
     *//*
    @Column(name = "storage_create")
    private Integer storageCreate;

    *//**
     * 同步配置状态
     *//*
    @Column(name = "sync_create")
    private Integer syncCreate;

    *//**
     * 迁移清除状态
     *//*
    @Column(name = "move_clean_create")
    private Integer moveCleanCreate;

    *//**
     * 备份恢复状态
     *//*
    @Column(name = "backup_create")
    private Integer backupCreate;

    *//**
     * 数据归档状态
     *//*
    @Column(name = "filed_create")
    private Integer filedCreate;*/

    /**
     * 资料属性
     */
    @Column(name = "data_prop", length = 50)
    private String dataProp;
}
