package com.piesat.schedule.rpc.dto.nas;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * @ClassName : NasManageFileDto
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-07 16:52
 */
@Data
public class NasManageFileDto{
    @ApiModelProperty(value = "用户ID" )
    private String userId;
    @ApiModelProperty(value = "用户名称" )
    private String userName;
    @ApiModelProperty(value = "nas配额硬性阀值" )
    private BigDecimal hardThreshold;
  /*  @ApiModelProperty(value = "nas配额软性阀值" )
    private BigDecimal softThreshold;
    @ApiModelProperty(value = "nas配额建议阀值" )
    private BigDecimal recommendedThreshold;*/
    @ApiModelProperty(value = "ip" )
    private String  ips;
    @ApiModelProperty(value = "是否docker" )
    private String isDocker;
    @ApiModelProperty(value = "ip数量" )
    private Integer ipNum;
    @ApiModelProperty(value = "权限" )
    private String permission;
    @ApiModelProperty(value = "申请材料" )
    @JSONField(serialize = false)
    private MultipartFile blFile;
}

