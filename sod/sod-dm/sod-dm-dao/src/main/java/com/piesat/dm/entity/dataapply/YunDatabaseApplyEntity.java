package com.piesat.dm.entity.dataapply;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "T_SOD_YUN_DATABASE_APPLY")
public class YunDatabaseApplyEntity extends BaseEntity {

    /**
     * 名称
     */
    @Column(name="displayname" ,length=50)
    private String displayname;
    /**
     * 实例服务名称
     */
    @Column(name="name" ,length=50)
    private String name;
    /**
     * 实例id
     */
    @Column(name="itservice_id" ,length=20)
    private String itserviceId;
    /**
     * 实例状态
     */
    @Column(name="itservice_status" ,length=20)
    private String itserviceStatus;
    /**
     * 实例类型
     */
    @Column(name = "storage_logic", length = 20)
    private String storageLogic;

    /**
     * 服务端口
     */
    @Column(name="port" ,length=10)
    private String port;
    /**
     * 管理服务端口
     */
    @Column(name="managerPort" ,length=10)
    private Integer managerPort;
    /**
     * API端口
     */
    @Column(name="restapiport" ,length=10)
    private Integer restApiport;
    /**
     * externalIP
     */
    @Column(name="externalIP" ,length=100)
    private String externalIP;
    /**
     * 集群规模
     */
    @Column(name="cluster_size" ,length=10)
    private String clusterSize;
    /**
     * CPU
     */
    @Column(name="cpu_size" ,length=10)
    private String cpuSize;
    /**
     * 内存
     */
    @Column(name="memory_size" ,length=10)
    private String memorySize;
    /**
     * 持久化存储空间大小
     */
    @Column(name="storage_size" ,length=10)
    private String storageSize;
    /**
     * portal用户名
     */
    @Column(name="user_name" ,length=50)
    private String webuserName;
    /**
     * 用户名
     */
    @Column(name="username" ,length=50)
    private String username;
    /**
     * 配置密码
     */
    @Column(name="password" ,length=50)
    private String password;
    /**
     * 客户端连接端口
     */
    @Column(name="externalport" ,length=10)
    private Integer externalPort;
    /**
     * 管理员密码
     */
    @Column(name="admin_password" ,length=10)
    private String adminPassword;
    /**
     * 初始化数据库名称
     */
    @Column(name="database" ,length=50)
    private String database;
    /**
     * 单位审核材料
     */
    @Column(name = "examine_material", length = 200)
    private String examineMaterial;
    /**
     * 模板
     */
    @Column(name = "template", length = 200)
    private String etemplate;
    /**
     * 数据库用途
     */
    @Column(name = "database_use", length = 200)
    private String databaseUse;

    /**
     * 审核状态
     * 包括：01-待审、02-已审、03-拒绝、04-申请释放、05-已释放
     */
    @Column(name = "examine_status", length = 20)
    private String examineStatus;

    /**
     * 拒绝原因
     */
    @Column(name = "reject_reason", length = 50)
    private String rejectReason;
    /**
     * 申请用户id
     */
    @Column(name = "user_id", length = 50)
    private String userId;
    /**
     * 审核时间
     */
    @Column(name = "examine_time")
    private Date examineTime;



    /**
     * 实际分配CPU
     */
    @Column(name = "cpu", length = 50)
    private String cpu;
    /**
     * 实际分配内存
     */
    @Column(name="memory" ,length=10)
    private String memory;

    /**
     * 实际分配存储空间大小
     * 单位为GB
     */
    @Column(name = "storage", length =20)
    private String storage;
    /**
     * 集群配置
     */
//    @Column(name="cluster" ,length=10)
//    private String cluster;
    /**
     * 集群规模
     */
    @Column(name="slaveCount" ,length=10)
    private String slaveCount;
    /**
     * 审核人
     */
    @Column(name = "examiner", length = 50)
    private String examiner;
}
