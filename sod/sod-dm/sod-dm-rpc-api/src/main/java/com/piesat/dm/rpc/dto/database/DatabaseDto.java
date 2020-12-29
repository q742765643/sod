package com.piesat.dm.rpc.dto.database;

import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.ConnectVo;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月21日 17:28:05
 */
@Data
public class DatabaseDto {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 数据库名称
     * database_name
     */
    private String databaseName;

    /**
     * 排序
     * serial_number
     */
    private Integer serialNumber;


    /**
     * 连接测试（1成功，2失败）
     * check_conn
     */
    private Integer checkConn;

    /**
     * 数据库实例
     * database_instance
     */
    private String databaseInstance;

    /**
     * 数据库类型
     * database_type
     */
    private String databaseType;

    /**
     * 主备类型
     * main_bak_type
     */
    private Integer mainBakType;

    /**
     * 存储容量
     * database_capacity
     */
    private Integer databaseCapacity;

    /**
     * 数据库描述
     * database_desc
     */
    private String databaseDesc;

    /**
     * 数据库驱动
     * driver_class_name
     */
    private String driverClassName;

    /**
     * portal显示控制
     * user_display_control
     */
    private Integer userDisplayControl;

    /**
     * 数据库ip
     * database_ip
     */
    private String databaseIp;

    /**
     * 数据库端口
     * database_port
     */
    private String databasePort;

    /**
     * 数据库连接url
     * database_url
     */
    private String databaseUrl;

    /**
     * up层访问路径
     * up_url
     */
    private String upUrl;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;

    private Set<DatabaseAdministratorDto> databaseAdministratorList;

    private Set<DatabaseNodesDto> databaseNodesList;

    private SchemaDto schemaDto;

    public ConnectVo getCoreInfo() {
        List<DatabaseAdministratorDto> managerUsers =
                this.databaseAdministratorList.stream()
                        .filter(DatabaseAdministratorDto::getIsManager)
                        .collect(Collectors.toList());
        if (managerUsers.size() == 0) {
            return null;
        }
        DatabaseAdministratorDto d = managerUsers.get(0);
        ConnectVo u = new ConnectVo();
        u.setPid(this.id);
        u.setDatabaseType(DatabaseTypesEnum.match(this.databaseType));
        u.setUrl(this.databaseUrl);
        u.setClassName(this.driverClassName);
        u.setPort(Integer.valueOf(this.databasePort));
        u.setIp(this.databaseIp);
        u.setUserName(d.getUserName());
        u.setPassWord(d.getPassWord());
        return u;
    }
}
