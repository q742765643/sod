package com.piesat.schedule.web.controller.policy;

import com.alibaba.fastjson.JSONArray;
import com.piesat.schedule.rpc.dto.policy.StrategyPolicyDto;
import com.piesat.schedule.rpc.service.policy.StrategyPolicyService;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-19 11:06
 **/
@RestController
@RequestMapping(value="/strategyPolicy")
@Api(value="存储策略接口",tags = {"存储策略接口"})
@Slf4j
public class StrategyPolicyController {
    @Autowired
    private StrategyPolicyService strategyPolicyService;

    @RequiresPermissions("schedule:strategyPolicy:strategyTree")
    @GetMapping("/strategyTree")
    @ApiOperation(value = "查询存储策略树", notes = "查询存储策略树")
    public ResultT strategyTree(){
        ResultT resultT=new ResultT();
        JSONArray json=strategyPolicyService.strategyTree();
        resultT.setData(json);
        return resultT;
    }

    @RequiresPermissions("schedule:strategyPolicy:findData")
    @GetMapping("/findData")
    @ApiOperation(value = "查询策略", notes = "查询策略")
    public ResultT<List<StrategyPolicyDto>> findData(String classId){
        ResultT<List<StrategyPolicyDto>> resultT=new ResultT();
        List<StrategyPolicyDto> strategyPolicyDtos=strategyPolicyService.findData(classId);
        resultT.setData(strategyPolicyDtos);
        return resultT;
    }

}

