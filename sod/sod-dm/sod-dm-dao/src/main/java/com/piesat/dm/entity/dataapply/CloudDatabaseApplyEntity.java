package com.piesat.dm.entity.dataapply;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/12 15:23
 */
@Entity
@Data
@Table(name = "T_SOD_CLOUD_DATABASE_APPLY")
public class CloudDatabaseApplyEntity extends BaseEntity {

    /**
     * 数据库名称
     */
    @Column(name = "database_name", length = 50)
    private String databaseName;

    /**
     * 数据库用途
     */
    @Column(name = "database_use", length = 200)
    private String databaseUse;

    /**
     * 申请用户id
     */
    @Column(name = "user_id", length = 50)
    private String userId;

    /**
     * 应用系统
     */
    @Column(name = "application_system", length = 50)
    private String applicationSystem;

    /**
     * 单位审核材料
     */
    @Column(name = "examine_material", length = 200)
    private String examineMaterial;

    /**
     * 申请CPU/内存
     */
    @Column(name = "cpu_memory", length = 50)
    private String cpuMemory;

    /**
     * 申请存储空间大小
     * 单位为GB
     */
    @Column(name = "storage_space", length = 20)
    private String storageSpace;

    /**
     * 实际分配CPU内存
     */
    @Column(name = "new_cpu_memory", length = 50)
    private String newCpuMemory;

    /**
     * 实际分配存储空间大小
     * 单位为GB
     */
    @Column(name = "new_storage_space", length =20)
    private String newStorageSpace;

    /**
     * 审核状态
     * 包括：01-待审、02-已审、03-拒绝、04-申请释放、05-已释放
     */
    @Column(name = "examine_status", length = 20)
    private String examineStatus;

    /**
     * 未通过原因
     * 当审核状态为拒绝时，这里写审核未通过原因
     */
    @Column(name = "failureReason", length = 200)
    private String failure_reason;

    /**
     * 审核人
     */
    @Column(name = "examiner", length = 50)
    private String examiner;

    /**
     * 审核时间
     */
    @Column(name = "examine_time")
    private Date examineTime;


    /**
     * 数据库IP
     */
    @Column(name = "database_ip", length = 50)
    private String databaseIp;

    /**
     * 数据库端口号
     */
    @Column(name = "database_port", length = 10)
    private Integer databasePort;

    /**
     * 数据库用户名
     */
    @Column(name = "database_username", length = 50)
    private String databaseUsername;

    /**
     * 数据库口令
     */
    @Column(name = "database_password", length = 50)
    private String databasePassword;

    /**
     * 云数据库类型
     * redis  网络共享存储  xugu
     */
    @Column(name = "storage_logic", length = 20)
    private String storageLogic;

    /**
     * 挂载服务器
     */
    @Column(name = "mount_server", length = 200)
    private String mountServer;

    /**
     * 挂载目录
     */
    @Column(name = "mount_directory", length = 50)
    private String mountDirectory;

}
