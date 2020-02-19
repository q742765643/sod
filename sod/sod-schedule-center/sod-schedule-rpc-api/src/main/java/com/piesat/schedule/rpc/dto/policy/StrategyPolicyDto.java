package com.piesat.schedule.rpc.dto.policy;

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
    private String databaseName;
    private String databaseId;
    private String dataClassId;
    List<PolicyDto> policyDtos;

}

