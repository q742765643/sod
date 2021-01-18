package com.piesat.dm.rpc.dto.dataapply;


import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("云数据库Dto")
public class YunDatabaseApplyDto extends BaseDto {

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String displayname;
    /**
     * 实例服务名称
     */
    @ApiModelProperty("实例服务名称")
    private String name;
    /**
     * 实例id
     */
    @ApiModelProperty("实例ID")
    private String itserviceId;
    /**
     * 实例状态
     */
    @ApiModelProperty("实例状态")
    private String itserviceStatus;

    /**
     * 实例类型
     */
    @ApiModelProperty("实例类型")
    private String storageLogic;


    /**
     * 服务端口
     */
    @ApiModelProperty("服务端口")
    private String port;
    /**
     * 管理服务端口
     */
    @ApiModelProperty("管理服务端口")
    private String managerPort;
    /**
     * API端口
     */
    @ApiModelProperty("API端口")
    private String restApiport;
    /**
     * 集群规模
     */
    @ApiModelProperty("集群规模")
    private String clusterSize;
    /**
     * CPU
     */
    @ApiModelProperty("CPU")
    private String cpuSize;
    /**
     * 内存
     */
    @ApiModelProperty("内存")
    private String memorySize;
    /**
     * 持久化存储空间大小
     */
    @ApiModelProperty("持久化存储空间大小")
    private String storageSize;
    /**
     * portal用户名
     */
    @ApiModelProperty("用户名")
    private String webuserName;
    /**
     * portal用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    /**
     * 配置密码
     */
    @ApiModelProperty("配置密码")
    private String password;
    /**
     * 客户端连接端口
     */
    @ApiModelProperty("客户端链接端口")
    private String externalPort;
    /**
     * 管理员密码
     */
    @ApiModelProperty("管理员密码")
    private String adminPassword;
    /**
     * 初始化数据库名称
     */
    @ApiModelProperty("初始化数据库名称")
    private String database;
    /**
     * 单位审核材料
     */
    @ApiModelProperty("单位审核材料")
    private String examineMaterial;
    /**
     * 模板
     */
    @ApiModelProperty("模板")
    private String etemplate;
    /**
     * 数据库用途
     */
    @ApiModelProperty("数据库用途")
    private String databaseUse;

    /**
     * 审核状态
     * 包括：01-待审、02-已审、03-拒绝、04-申请释放、05-已释放
     */
    @ApiModelProperty("审核状态")
    private String examineStatus;

    /**
     * 拒绝原因
     */
    @ApiModelProperty("拒绝原因")
    private String rejectReason;
    /**
     * 申请用户id
     */
    @ApiModelProperty( "申请用户ID")
    private String userId;
    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private Date examineTime;




    /**
     * 实际分配CPU内存
     */
    @ApiModelProperty("实际分配CPU内存")
    private String cpu;
    /**
     * 实际分配内存
     */
    @ApiModelProperty("实际分配内存" )
    private String memory;

    /**
     * 实际分配存储空间大小
     * 单位为GB
     */
    @ApiModelProperty("实际分配存储空间大小")
    private String storage;
    /**
     * 集群配置
     */
//    @ApiModelProperty("集群规配置")
//    private String cluster;
    /**
     * 集群规模
     */
    @ApiModelProperty("集群规模")
    private String slaveCount;
    /**
     * 审核人
     */
    @ApiModelProperty("审核人")
    private String examiner;

    /**
     * 审核人
     */
    @ApiModelProperty("externalIP")
    private String externalIP;


    private String telephone;//申请用户联系方式
    private String department;//申请用户所属部门


}
