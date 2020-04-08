package com.piesat.dm.web.controller.database;

import com.piesat.dm.rpc.api.database.DatabaseNodesService;
import com.piesat.dm.rpc.dto.database.DatabaseNodesDto;
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
 * 数据库节点信息
 *
 * @author cwh
 * @date 2019年 11月29日 09:45:11
 */
@Api(tags = "数据库节点管理")
@RequestMapping("/dm/databaseNodes")
@RestController
public class DatabaseNodesController {
    @Autowired
    private DatabaseNodesService databaseNodesService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:databaseNodes:add")
    @Log(title = "数据库节点管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DatabaseNodesDto databaseNodesDto) {
        try {
            DatabaseNodesDto save = this.databaseNodesService.saveDto(databaseNodesDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:databaseNodes:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DatabaseNodesDto databaseNodesDto = this.databaseNodesService.getDotById(id);
            return ResultT.success(databaseNodesDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据物理库id查询")
    @RequiresPermissions("dm:databaseNodes:findByDatabaseId")
    @GetMapping(value = "/findByDatabaseId")
    public ResultT findByDatabaseId(String id) {
        try {
            List<DatabaseNodesDto> databaseNodesDto = this.databaseNodesService.findByDatabaseId(id);
            return ResultT.success(databaseNodesDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:databaseNodes:del")
    @Log(title = "数据库节点管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.databaseNodesService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:databaseNodes:all")
    @GetMapping(value = "/all")
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
