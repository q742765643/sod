package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据库定义
 *
 * @author cwh
 * @date 2019年 11月21日 17:28:05
 */
@Data
@Table(name = "T_SOD_DATABASE_DEFINE")
@Entity
public class DatabaseDefineEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库标识
     * database_id
     */
    @Column(name = "database_id", length = 36, nullable = false)
    private String databaseId;

    /**
     * 数据库名称
     * database_name
     */
    @Column(name = "database_name", length = 36, nullable = false)
    private String databaseName;

    /**
     * 排序
     * serial_number
     */
    @Column(name = "serial_number")
    private Integer serialNumber;


    /**
     * 连接测试（1成功，2失败）
     * check_conn
     */
    @Column(name = "check_conn")
    private Integer checkConn;

    /**
     * 数据库实例
     * database_instance
     */
    @Column(name = "database_instance", length = 36, nullable = false)
    private String databaseInstance;

    /**
     * 数据库类型
     * database_type
     */
    @Column(name = "database_type", length = 36, nullable = false)
    private String databaseType;

    /**
     * 主备类型
     * main_bak_type
     */
    @Column(name = "main_bak_type")
    private Integer mainBakType;

    /**
     * 存储容量
     * database_capacity
     */
    @Column(name = "database_capacity")
    private Integer databaseCapacity;

    /**
     * 数据库描述
     * database_desc
     */
    @Column(name = "database_desc", length = 255)
    private String databaseDesc;

    /**
     * 数据库驱动
     * driver_class_name
     */
    @Column(name = "driver_class_name", length = 255, nullable = false)
    private String driverClassName;

    /**
     * portal显示控制
     * user_display_control
     */
    @Column(name = "user_display_control")
    private Integer userDisplayControl;

    /**
     * 数据库ip
     * database_ip
     */
    @Column(name = "database_ip", length = 36, nullable = false)
    private String databaseIp;

    /**
     * 数据库端口
     * database_port
     */
    @Column(name = "database_port", length = 36, nullable = false)
    private String databasePort;

    /**
     * 数据库连接url
     * database_url
     */
    @Column(name = "database_url", length = 255, nullable = false)
    private String databaseUrl;

    /**
     * up层访问路径
     * up_url
     */
    @Column(name = "up_url", length = 255)
    private String upUrl;
}
