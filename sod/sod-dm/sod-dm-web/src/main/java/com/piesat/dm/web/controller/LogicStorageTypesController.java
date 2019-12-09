package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.LogicStorageTypesService;
import com.piesat.dm.rpc.dto.LogicStorageTypesDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据用途与存储类型对应
 *
 * @author cwh
 * @date 2019年 11月29日 10:21:15
 */
@RestController
@Api(value = "数据用途与存储类型对应controller", tags = {"数据用途与存储类型对应操作接口"})
public class LogicStorageTypesController {
    @Autowired
    private LogicStorageTypesService logicStorageTypesService;

    @PostMapping(value = "/api/logicStorage/save")
    @ApiOperation(value = "添加数据用途与存储类型对应接口", notes = "添加数据用途与存储类型对应接口")
    public ResultT save(@RequestBody LogicStorageTypesDto logicStorageTypesDto) {
        try {
            logicStorageTypesDto = this.logicStorageTypesService.saveDto(logicStorageTypesDto);
            return ResultT.success(logicStorageTypesDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/logicStorage/get")
    @ApiOperation(value = "获取数据用途与存储类型对应接口", notes = "获取数据用途与存储类型对应接口")
    public ResultT get(String id) {
        try {
            LogicStorageTypesDto logicStorageTypesDto = this.logicStorageTypesService.getDotById(id);
            return ResultT.success(logicStorageTypesDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/logicStorage/del")
    @ApiOperation(value = "删除数据用途与存储类型对应接口", notes = "删除数据用途与存储类型对应接口")
    public ResultT del(String id) {
        try {
            this.logicStorageTypesService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/logicStorage/all")
    @ApiOperation(value = "获取所有数据用途与存储类型对应接口", notes = "获取所有数据用途与存储类型对应接口")
    public ResultT all() {
        try {
            List<LogicStorageTypesDto> all = this.logicStorageTypesService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
