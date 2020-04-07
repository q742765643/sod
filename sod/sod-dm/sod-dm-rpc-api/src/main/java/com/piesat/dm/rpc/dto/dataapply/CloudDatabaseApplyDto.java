package com.piesat.dm.rpc.dto.dataapply;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/12 16:02
 */
@Data
@ApiModel("云数据库审核DTO")
public class CloudDatabaseApplyDto extends BaseDto {
    /**
     * 数据库名称
     */
    @ApiModelProperty("数据库名称")
    private String databaseName;

    /**
     * 数据库用途
     */
    @ApiModelProperty("数据库用途")
    private String databaseUse;

    /**
     * 申请用户id
     */
    @ApiModelProperty("申请用户id")
    private String userId;

    /**
     * 应用系统
     */
    @ApiModelProperty("应用系统")
    private String applicationSystem;

    /**
     * 单位审核材料
     */
    @ApiModelProperty("单位审核材料")
    private String examineMaterial;

    /**
     * 申请CPU/内存
     */
    @ApiModelProperty("申请CPU/内存")
    private String cpuMemory;

    /**
     * 申请存储空间大小
     * 单位为GB
     */
    @ApiModelProperty("申请存储空间大小")
    private String storageSpace;

    /**
     * 实际分配CPU内存
     */
    @ApiModelProperty("实际分配CPU内存")
    private String newCpuMemory;

    /**
     * 实际分配存储空间大小
     * 单位为GB
     */
    @ApiModelProperty("实际分配存储空间大小")
    private String newStorageSpace;

    /**
     * 审核状态
     * 包括：01-待审、02-已审、03-拒绝
     */
    @ApiModelProperty("审核状态")
    private String examineStatus;

    /**
     * 未通过原因
     * 当审核状态为拒绝时，这里写审核未通过原因
     */
    @ApiModelProperty("未通过原因")
    private String failure_reason;

    /**
     * 审核人
     */
    @ApiModelProperty("审核人")
    private String examiner;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private Date examineTime;


    /**
     * 数据库IP
     */
    @ApiModelProperty("数据库IP")
    private String databaseIp;

    /**
     * 数据库端口号
     */
    @ApiModelProperty("数据库端口号")
    private Integer databasePort;

    /**
     * 数据库用户名
     */
    @ApiModelProperty("数据库用户名")
    private String databaseUsername;

    /**
     * 数据库口令
     */
    @ApiModelProperty("数据库口令")
    private String databasePassword;

    /**
     * 云数据库类型
     * redis  网络共享存储  xugu
     */
    @ApiModelProperty("云数据库类型")
    private String storageLogic;

    /**
     * 挂载服务器
     */
    @ApiModelProperty("挂载服务器")
    private String mountServer;

    /**
     * 挂载目录
     */
    @ApiModelProperty("挂载目录")
    private String mountDirectory;

    private String userName;//申请用户名称
    private String telephone;//申请用户联系方式
    private String department;//申请用户所属部门
}
