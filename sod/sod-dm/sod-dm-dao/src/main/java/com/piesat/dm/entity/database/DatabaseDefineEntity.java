package com.piesat.dm.entity.database;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月21日 17:28:05
 */
@Data
@Table(name = "T_SOD_DATABASE_DEFINE")
@Entity
@Proxy(lazy = false)
public class DatabaseDefineEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库名称
     * database_name
     */
    @Excel(name = "数据库名称")
    @Column(name = "database_name", length = 36, nullable = false)
    private String databaseName;

    /**
     * 排序
     * serial_number
     */
    @Excel(name = "排序")
    @Column(name = "serial_number")
    private Integer serialNumber;


    /**
     * 连接测试（1成功，2失败）
     * check_conn
     */
    @Excel(name = "连接测试（1成功，2失败）")
    @Column(name = "check_conn")
    private Integer checkConn;

    /**
     * 数据库实例
     * database_instance
     */
    @Excel(name = "数据库实例")
    @Column(name = "database_instance", length = 36, nullable = false)
    private String databaseInstance;

    /**
     * 数据库类型
     * database_type
     */
    @Excel(name = "数据库类型")
    @Column(name = "database_type", length = 36, nullable = false)
    private String databaseType;

    /**
     * 主备类型
     * main_bak_type
     */
    @Excel(name = "主备类型")
    @Column(name = "main_bak_type")
    private Integer mainBakType;

    /**
     * 存储容量
     * database_capacity
     */
    @Excel(name = "存储容量")
    @Column(name = "database_capacity")
    private Integer databaseCapacity;

    /**
     * 数据库描述
     * database_desc
     */
    @Excel(name = "数据库描述")
    @Column(name = "database_desc", length = 255)
    private String databaseDesc;

    /**
     * 数据库驱动
     * driver_class_name
     */
    @Excel(name = "数据库驱动")
    @Column(name = "driver_class_name", length = 255, nullable = false)
    private String driverClassName;

    /**
     * portal显示控制
     * user_display_control
     * 1 显示   2 不显示   3 前端不显示
     */
    @Excel(name = "portal显示控制")
    @Column(name = "user_display_control")
    private Integer userDisplayControl;

    /**
     * 数据库ip
     * database_ip
     */
    @Excel(name = "数据库ip")
    @Column(name = "database_ip", length = 36, nullable = false)
    private String databaseIp;

    /**
     * 数据库端口
     * database_port
     */
    @Excel(name = "数据库端口")
    @Column(name = "database_port", length = 36, nullable = false)
    private String databasePort;

    /**
     * 数据库连接url
     * database_url
     */
    @Excel(name = "数据库连接url")
    @Column(name = "database_url", length = 655, nullable = false)
    private String databaseUrl;

    /**
     * up层访问路径
     * up_url
     */
    @Excel(name = "up层访问路径")
    @Column(name = "up_url", length = 255)
    private String upUrl;

    @OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
    @JoinColumn(name="database_id")
    private Set<DatabaseAdministratorEntity> databaseAdministratorList = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
    @JoinColumn(name="database_id")
    private Set<DatabaseNodesEntity> databaseNodesList = new HashSet<>();
}
