package com.piesat.schedule.web.controller.job;

import com.piesat.common.utils.OwnException;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.service.DataBaseService;
import com.piesat.schedule.rpc.vo.DataRetrieval;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-25 15:08
 **/
@RestController
@Api(value="迁移备份清除启停接口",tags = {"迁移备份清除启停接口"})
@RequestMapping("/schedule/job")
@Slf4j
public class JobInfoController {
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private DataBaseService dataBaseService;

    @GetMapping("/findAllDataBase")
    @ApiOperation(value = "查询所有物理库接口", notes = "查询所有物理库接口")
    public ResultT findAllDataBase(){
        ResultT resultT=new ResultT();
        List<DatabaseDto> databaseDtos= (List<DatabaseDto>) dataBaseService.findAllDataBase();
        resultT.setData(databaseDtos);
        return resultT;
    }
    @GetMapping(value = "/getByDatabaseId/{databaseId}")
    public ResultT getByDatabaseId(@PathVariable String databaseId){
        ResultT resultT=new ResultT();
        List<Map<String, Object>> mapList=dataBaseService.getByDatabaseId(databaseId);
        resultT.setData(mapList);
        return resultT;
    }
    @ApiOperation(value = "查询所有物理库详情接口", notes = "查询所有物理库详情接口")
    @GetMapping(value = "/findDataBaseById/{databaseId}")
    public  ResultT<DatabaseDto> findDataBaseById(@PathVariable String databaseId){
        ResultT<DatabaseDto> resultT=new ResultT();
        DatabaseDto databaseDto=dataBaseService.findDataBaseById(databaseId);
        resultT.setData(databaseDto);
        return resultT;
    }
    @GetMapping(value = "/getByDatabaseIdAndClassId")
    @ApiOperation(value = "查询资料详情接口", notes = "查询资料详情接口")
    public ResultT getByDatabaseIdAndClassId(String databaseId,String dataClassId){
        ResultT resultT=new ResultT();
        DataRetrieval dataRetrieval =dataBaseService.getByDatabaseIdAndClassId(databaseId,dataClassId);
        resultT.setData(dataRetrieval);
        return resultT;
    }
    @GetMapping(value = "/startById")
    @ApiOperation(value = "启动任务接口", notes = "启动任务接口")
    @RequiresPermissions("schedule:job:startById")
    public ResultT<String> startById(String id){
        ResultT resultT=new ResultT();
        try {
            jobInfoService.startById(id);
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage("启动失败");
        }
        return resultT;
    }
    @GetMapping(value = "/stop")
    @ApiOperation(value = "停止任务接口", notes = "停止任务接口")
    @RequiresPermissions("schedule:job:stop")
    public ResultT<String> stop(String id){
        ResultT resultT=new ResultT();
        try {
            jobInfoService.stopById(id);
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage("停止失败");
        }
        return resultT;
    }
    @GetMapping(value = "/execute")
    @RequiresPermissions("schedule:job:execute")
    @ApiOperation(value = "立即执行接口", notes = "立即执行接口")
    public ResultT<String> execute(String id){
        ResultT resultT=new ResultT();
        try {
            jobInfoService.execute(id);
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage("立即执行失败");
        }
        return resultT;
    }
}

