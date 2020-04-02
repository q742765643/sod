package com.piesat.dm.web.controller.database;

import com.piesat.dm.rpc.api.database.DatabaseAdministratorService;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
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
 * 数据库账户
 *
 * @author cwh
 * @date 2019年 11月28日 10:58:33
 */

@Api(tags = "数据库账户管理")
@RestController
@RequestMapping("/dm/administrator")
public class DatabaseAdministratorController {
    @Autowired
    private DatabaseAdministratorService databaseAdministratorService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:administrator:add")
    @Log(title = "数据库账户管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DatabaseAdministratorDto databaseAdministratorDto) {
        try {
            DatabaseAdministratorDto save = this.databaseAdministratorService.saveDto(databaseAdministratorDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id获取")
    @RequiresPermissions("dm:administrator:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DatabaseAdministratorDto databaseAdministratorDto = this.databaseAdministratorService.getDotById(id);
            return ResultT.success(databaseAdministratorDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @Log(title = "数据库账户管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.databaseAdministratorService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @GetMapping(value = "/all")
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
