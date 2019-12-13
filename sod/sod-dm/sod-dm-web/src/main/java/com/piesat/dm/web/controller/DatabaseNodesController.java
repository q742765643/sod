package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DatabaseNodesService;
import com.piesat.dm.rpc.dto.DatabaseNodesDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据库节点信息
 *
 * @author cwh
 * @date 2019年 11月29日 09:45:11
 */
@RestController
@Api(value = "数据库节点controller", tags = {"数据库节点操作接口"})
public class DatabaseNodesController {
    @Autowired
    private DatabaseNodesService databaseNodesService;

    @PostMapping(value = "/api/databaseNodes/save")
    @ApiOperation(value = "添加数据库节点接口", notes = "添加数据库节点接口")
    public ResultT save(@RequestBody DatabaseNodesDto databaseNodesDto) {
        try {
            DatabaseNodesDto save = this.databaseNodesService.saveDto(databaseNodesDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/databaseNodes/get")
    @ApiOperation(value = "获取数据库节点接口", notes = "获取数据库节点接口")
    public ResultT get(String id) {
        try {
            DatabaseNodesDto databaseNodesDto = this.databaseNodesService.getDotById(id);
            return ResultT.success(databaseNodesDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/databaseNodes/del")
    @ApiOperation(value = "删除数据库节点接口", notes = "删除数据库节点接口")
    public ResultT del(String id) {
        try {
            this.databaseNodesService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/databaseNodes/all")
    @ApiOperation(value = "获取所有数据库节点接口", notes = "获取所有数据库节点接口")
    public ResultT all() {
        try {
            List<DatabaseNodesDto> all = this.databaseNodesService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
