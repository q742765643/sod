package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DatabaseAdministratorService;
import com.piesat.dm.rpc.dto.DatabaseAdministratorDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据库账户
 *
 * @author cwh
 * @date 2019年 11月28日 10:58:33
 */
@RestController
@Api(value = "数据库账户controller", tags = {"数据库账户操作接口"})
public class DatabaseAdministratorController {
    @Autowired
    private DatabaseAdministratorService databaseAdministratorService;

    @PostMapping(value = "/api/administrator/save")
    @ApiOperation(value = "添加数据库账户接口", notes = "添加数据库账户接口")
    public ResultT save(@RequestBody DatabaseAdministratorDto databaseAdministratorDto) {
        try {
            DatabaseAdministratorDto save = this.databaseAdministratorService.saveDto(databaseAdministratorDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/administrator/get")
    @ApiOperation(value = "获取数据库账户接口", notes = "获取数据库账户接口")
    public ResultT get(String id) {
        try {
            DatabaseAdministratorDto databaseAdministratorDto = this.databaseAdministratorService.getDotById(id);
            return ResultT.success(databaseAdministratorDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/administrator/del")
    @ApiOperation(value = "删除数据库账户接口", notes = "删除数据库账户接口")
    public ResultT del(String id) {
        try {
            this.databaseAdministratorService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/administrator/all")
    @ApiOperation(value = "获取所有数据库账户接口", notes = "获取所有数据库账户接口")
    public ResultT all() {
        try {
            List<DatabaseAdministratorDto> all = this.databaseAdministratorService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
