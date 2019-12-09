package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据用途
 *
 * @author cwh
 * @date 2019年 11月25日 14:18:09
 */
@RestController
@Api(value="数据用途controller",tags={"数据用途操作接口"})
public class DataLogicController {
    @Autowired
    private DataLogicService dataLogicService;
    @PostMapping(value = "/api/dataLogic/save")
    @ApiOperation(value="添加数据用途接口", notes="添加数据用途接口")
    public ResultT save (@RequestBody DataLogicDto dataLogicDto){
        try {
            dataLogicDto = this.dataLogicService.saveDto(dataLogicDto);
            return ResultT.success(dataLogicDto);
        }catch (Exception e){
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @PostMapping(value = "/api/dataLogic/get")
    @ApiOperation(value = "获取数据用途接口", notes = "获取数据用途接口")
    public ResultT get(String id) {
        try {
            DataLogicDto dataLogicDto = this.dataLogicService.getDotById(id);
            return ResultT.success(dataLogicDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/dataLogic/del")
    @ApiOperation(value = "删除数据用途接口", notes = "删除数据用途接口")
    public ResultT del(String id) {
        try {
            this.dataLogicService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/dataLogic/all")
    @ApiOperation(value = "获取所有数据用途接口", notes = "获取所有数据用途接口")
    public ResultT all() {
        try {
            List<DataLogicDto> all = this.dataLogicService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
