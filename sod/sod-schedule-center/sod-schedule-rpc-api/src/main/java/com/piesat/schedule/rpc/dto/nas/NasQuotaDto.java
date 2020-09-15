package com.piesat.schedule.rpc.dto.nas;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName : NasQuotaDto
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-14 17:36
 */
@Data
public class NasQuotaDto extends BaseDto {
    @ApiModelProperty(name = "私有目录")
    private String privateDirectory;
    @ApiModelProperty(name = "nas厂商")
    private String nasVendor;
    @ApiModelProperty(name = "nas硬性配额")
    private BigDecimal hardThreshold;
    @ApiModelProperty(name = "申请材料路径")
    private String reviewMaterials;
    @ApiModelProperty(name = "申请材料pdf")
    private String reviewMaterialsPdf;
    @ApiModelProperty(name = "审核结果")
    private String auditResults;
    @ApiModelProperty(name = "审核时间")
    private Date auditTime;
    @ApiModelProperty(name = "nas状态")
    private String resourceStatus;
    @ApiModelProperty(name = "nasId")
    private String nasId;
}

