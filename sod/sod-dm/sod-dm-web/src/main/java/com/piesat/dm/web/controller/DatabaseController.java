package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据库基础库专题库
 *
 * @author cwh
 * @date 2019年 11月29日 09:48:31
 */
@RestController
@Api(value = "数据库controller", tags = {"数据库操作接口"})
public class DatabaseController {
    @Autowired
    private DatabaseService databaseService;

    @PostMapping(value = "/api/database/save")
    @ApiOperation(value = "添加数据库接口", notes = "添加数据库接口")
    public ResultT save(@RequestBody DatabaseDto databaseDto) {
        try {
            DatabaseDto save = this.databaseService.saveDto(databaseDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/database/get")
    @ApiOperation(value = "获取数据库接口", notes = "获取数据库接口")
    public ResultT get(String id) {
        try {
            DatabaseDto databaseDto = this.databaseService.getDotById(id);
            return ResultT.success(databaseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/database/del")
    @ApiOperation(value = "删除数据库接口", notes = "删除数据库接口")
    public ResultT del(String id) {
        try {
            this.databaseService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/database/all")
    @ApiOperation(value = "获取所有数据库接口", notes = "获取所有数据库接口")
    public ResultT all() {
        try {
            List<DatabaseDto> all = this.databaseService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
