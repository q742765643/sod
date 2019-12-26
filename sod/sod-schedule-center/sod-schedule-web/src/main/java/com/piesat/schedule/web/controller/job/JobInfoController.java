package com.piesat.schedule.web.controller.job;

import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-25 15:08
 **/
@RestController
@RequestMapping("/schedule/job")
public class JobInfoController {
    @Autowired
    private JobInfoService jobInfoService;

    @GetMapping("/findAllDataBase")
    public ResultT findAllDataBase(){
        ResultT resultT=new ResultT();
        List<DatabaseDto> databaseDtos=jobInfoService.findAllDataBase();
        resultT.setData(databaseDtos);
        return resultT;
    }
    @GetMapping(value = "/getByDatabaseId/{databaseId}")
    public ResultT getByDatabaseId(@PathVariable String databaseId){
        ResultT resultT=new ResultT();
        List<DataLogicDto> dataLogicDtos=jobInfoService.getByDatabaseId(databaseId);
        resultT.setData(dataLogicDtos);
        return resultT;
    }
}

