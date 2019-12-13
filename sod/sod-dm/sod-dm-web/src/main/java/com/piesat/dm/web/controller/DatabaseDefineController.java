package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DatabaseDefineService;
import com.piesat.dm.rpc.dto.DatabaseDefineDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月29日 09:41:21
 */
@RestController
@Api(value = "数据库类型controller", tags = {"数据库类型操作接口"})
public class DatabaseDefineController {
    @Autowired
    private DatabaseDefineService databaseDefineService;

    @PostMapping(value = "/api/databaseDefine/save")
    @ApiOperation(value = "添加数据库类型接口", notes = "添加数据库类型接口")
    public ResultT save(@RequestBody DatabaseDefineDto databaseDefineDto) {
        try {
            DatabaseDefineDto save = this.databaseDefineService.saveDto(databaseDefineDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/databaseDefine/get")
    @ApiOperation(value = "获取数据库类型接口", notes = "获取数据库类型接口")
    public ResultT get(String id) {
        try {
            DatabaseDefineDto DatabaseDefineDto = this.databaseDefineService.getDotById(id);
            return ResultT.success(DatabaseDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/databaseDefine/del")
    @ApiOperation(value = "删除数据库类型接口", notes = "删除数据库类型接口")
    public ResultT del(String id) {
        try {
            this.databaseDefineService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/databaseDefine/all")
    @ApiOperation(value = "获取所有数据库类型接口", notes = "获取所有数据库类型接口")
    public ResultT all() {
        try {
            List<DatabaseDefineDto> all = this.databaseDefineService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
