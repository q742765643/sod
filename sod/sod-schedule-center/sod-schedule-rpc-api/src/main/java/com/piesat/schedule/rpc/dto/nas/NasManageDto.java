package com.piesat.schedule.rpc.dto.nas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class NasManageDto extends BaseDto {
    @ApiModelProperty(value = "用户ID" )
    private String userId;
    @ApiModelProperty(value = "用户名称" )
    private String userName;
    @ApiModelProperty(value = "nas配额硬性阀值" )
    private BigDecimal hardThreshold;
   /* @ApiModelProperty(value = "nas配额软性阀值" )
    private BigDecimal softThreshold;
    @ApiModelProperty(value = "nas配额建议阀值" )
    private BigDecimal recommendedThreshold;*/
    @ApiModelProperty(value = "审核状态 0 未审核 1审核通过 2 审核不通过" )
    private String auditStatus;
    @ApiModelProperty(value = "申请目录" )
    private String privateDirectory;
    @ApiModelProperty(value = "审核人" )
    private String  auditor;
    @ApiModelProperty(value = "审核意见" )
    private String  auditOpinion;
    @ApiModelProperty(value = "ip" )
    private String  ips;
    @ApiModelProperty(value = "是否docker 0 主机 1 docker" )
    private String isDocker;
    @ApiModelProperty(value = "ip数量" )
    private Integer ipNum;
 /*   @ApiModelProperty(value = "权限 多选 0 读权限 1写权限" )
    private String permission;*/
    @ApiModelProperty(value = "申请材料路径" )
    private String reviewMaterials;
    @ApiModelProperty(value = "申请材料pdf路径" )
    private String reviewMaterialsPdf;
    @ApiModelProperty(value = "审核结果" )
    private String auditResults;
    @ApiModelProperty(value = "审核时间" )
    private Date auditTime;

}
