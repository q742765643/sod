package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.LogicDefineService;
import com.piesat.dm.rpc.dto.LogicDefineDto;
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
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月25日 14:46:04
 */
@Api(tags = "数据用途定义")
@RequestMapping("/dm/logicDefine")
@RestController
public class LogicDefineController {
    @Autowired
    private LogicDefineService logicDefineService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:logicDefine:add")
    @Log(title = "数据用途定义", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody LogicDefineDto logicDefineDto) {
        try {
            logicDefineDto = this.logicDefineService.saveDto(logicDefineDto);
            return ResultT.success(logicDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:logicDefine:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            LogicDefineDto logicDefineDto = this.logicDefineService.getDotById(id);
            return ResultT.success(logicDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:logicDefine:del")
    @Log(title = "数据用途定义", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.logicDefineService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:logicDefine:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<LogicDefineDto> allLogicDefine = this.logicDefineService.getAllLogicDefine();
            return ResultT.success(allLogicDefine);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
