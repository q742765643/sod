package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DatabaseDefineService;
import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.dto.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据库基础库专题库
 *
 * @author cwh
 * @date 2019年 11月29日 09:48:31
 */
@Api(tags = "数据库管理")
@RequestMapping("/dm/database")
@RestController
public class DatabaseController {
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private DatabaseDefineService databaseDefineService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:database:add")
    @Log(title = "数据库管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DatabaseDto databaseDto) {
        try {
            DatabaseDefineDto databaseDefine = databaseDefineService.getDotById(databaseDto.getDatabaseDefine().getId());
            databaseDto.setDatabaseDefine(databaseDefine);
            DatabaseDto save = this.databaseService.saveDto(databaseDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:database:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DatabaseDto databaseDto = this.databaseService.getDotById(id);
            return ResultT.success(databaseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:database:del")
    @Log(title = "数据库管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.databaseService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:database:all")
    @GetMapping(value = "/all")
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
