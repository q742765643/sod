package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.TableForeignKeyService;
import com.piesat.dm.rpc.dto.TableForeignKeyDto;
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
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:14:12
 */
@Api(tags = "外键关联")
@RequestMapping("/dm/foreignKey")
@RestController
public class TableForeignKeyController {
    @Autowired
    private TableForeignKeyService tableForeignKeyService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:foreignKey:add")
    @Log(title = "外键关联", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody TableForeignKeyDto TableForeignKeyDto) {
        try {
            TableForeignKeyDto save = this.tableForeignKeyService.saveDto(TableForeignKeyDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:foreignKey:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            TableForeignKeyDto TableForeignKeyDto = this.tableForeignKeyService.getDotById(id);
            return ResultT.success(TableForeignKeyDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:foreignKey:del")
    @Log(title = "外键关联", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.tableForeignKeyService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:foreignKey:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<TableForeignKeyDto> all = this.tableForeignKeyService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据tableId查询")
    @RequiresPermissions("dm:foreignKey:findByTableId")
    @GetMapping(value = "/findByTableId")
    public ResultT findByTableId(String tableId){
        try {
            List<TableForeignKeyDto> all = this.tableForeignKeyService.findByTableId(tableId);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
