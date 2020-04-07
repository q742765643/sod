package com.piesat.dm.web.controller.dataclass;

import com.piesat.dm.rpc.api.dataclass.LogicStorageTypesService;
import com.piesat.dm.rpc.dto.dataclass.LogicStorageTypesDto;
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
 * 数据用途与存储类型对应
 *
 * @author cwh
 * @date 2019年 11月29日 10:21:15
 */
@Api(tags = "数据用途与存储类型")
@RequestMapping("/dm/logicStorage")
@RestController
public class LogicStorageTypesController {
    @Autowired
    private LogicStorageTypesService logicStorageTypesService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:logicStorage:add")
    @Log(title = "数据用途与存储类型", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody LogicStorageTypesDto logicStorageTypesDto) {
        try {
            logicStorageTypesDto = this.logicStorageTypesService.saveDto(logicStorageTypesDto);
            return ResultT.success(logicStorageTypesDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:logicStorage:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            LogicStorageTypesDto logicStorageTypesDto = this.logicStorageTypesService.getDotById(id);
            return ResultT.success(logicStorageTypesDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:logicStorage:del")
    @Log(title = "数据用途与存储类型", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.logicStorageTypesService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:logicStorage:all")
    @GetMapping(value = "/all")
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
