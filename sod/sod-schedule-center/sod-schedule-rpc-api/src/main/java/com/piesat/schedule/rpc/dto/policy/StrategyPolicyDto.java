package com.piesat.schedule.rpc.dto.policy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-19 14:35
 **/
@Data
public class StrategyPolicyDto {
    @ApiModelProperty("物理库名称")
    private String databaseName;
    @ApiModelProperty("物理库ID")
    private String databaseId;
    @ApiModelProperty("存储编码")
    private String dataClassId;
    List<PolicyDto> policyDtos;

}

