package com.piesat.schedule.rpc.dto.nas;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class NasDetailsDto extends BaseDto {
    @ApiModelProperty(value = "用户ID" )
    private String userId;
    @ApiModelProperty(value = "用户名称" )
    private String userName;
/*    @ApiModelProperty(value = "nas配额硬性阀值" )
    private BigDecimal hardThreshold;*/
  /*  @ApiModelProperty(value = "nas配额软性阀值" )
    private BigDecimal softThreshold;
    @ApiModelProperty(value = "nas配额建议阀值" )
    private BigDecimal recommendedThreshold;*/
    @ApiModelProperty(value = "目录" )
    private String privateDirectory;
    @ApiModelProperty(value = "ip" )
    private String  ips;
 /*   @ApiModelProperty(value = "nas厂商信息" )
    private String nasVendor;*/
    @ApiModelProperty(value = "是否容器" )
    private String isDocker;
    @ApiModelProperty(value = "acl状态" )
    private String aclStatus;
  /*  @ApiModelProperty(value = "权限" )
    private String permission;*/
/*    @ApiModelProperty(value = "申请材料路径" )
    private String reviewMaterials;
    @ApiModelProperty(value = "申请材料pdf路径" )
    private String reviewMaterialsPdf;
    @ApiModelProperty(value = "配额状态" )
    private String resourceStatus;*/
}
