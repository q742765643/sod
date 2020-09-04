package com.piesat.schedule.rpc.dto.nas;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NasManageDto extends BaseDto {
    @ApiModelProperty(value = "用户ID" )
    private String userId;
    @ApiModelProperty(value = "用户名称" )
    private String userName;
    @ApiModelProperty(value = "nas配额硬性阀值" )
    private BigDecimal hardThreshold;
    @ApiModelProperty(value = "nas配额软性阀值" )
    private BigDecimal softThreshold;
    @ApiModelProperty(value = "nas配额建议阀值" )
    private BigDecimal recommendedThreshold;
    @ApiModelProperty(value = "审核状态" )
    private String auditStatus;
    @ApiModelProperty(value = "目录" )
    private String privateDirectory;
    @ApiModelProperty(value = "审核人" )
    private String  auditor;
    @ApiModelProperty(value = "审核意见" )
    private String  auditOpinion;
    @ApiModelProperty(value = "ip" )
    private String  ips;
    @ApiModelProperty(value = "是否docker" )
    private String isDocker;
    @ApiModelProperty(value = "ip数量" )
    private Integer ipNum;
    @ApiModelProperty(value = "权限" )
    private String permission;
    @ApiModelProperty(value = "申请路径" )
    private String reviewMaterials;
}
