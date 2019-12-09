package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.LogicDefineService;
import com.piesat.dm.rpc.dto.LogicDefineDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月25日 14:46:04
 */
@RestController
@Api(value = "数据用途定义controller", tags = {"数据用途定义操作接口"})
public class LogicDefineController {
    @Autowired
    private LogicDefineService logicDefineService;

    @PostMapping(value = "/api/logicDefine/save")
    @ApiOperation(value = "添加数据用途定义接口", notes = "添加数据用途定义接口")
    public ResultT save(@RequestBody LogicDefineDto logicDefineDto) {
        try {
            logicDefineDto = this.logicDefineService.saveDto(logicDefineDto);
            return ResultT.success(logicDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/logicDefine/get")
    @ApiOperation(value = "获取数据用途接口", notes = "获取数据用途接口")
    public ResultT get(String id) {
        try {
            LogicDefineDto logicDefineDto = this.logicDefineService.getDotById(id);
            return ResultT.success(logicDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/logicDefine/del")
    @ApiOperation(value = "删除数据用途接口", notes = "删除数据用途接口")
    public ResultT del(String id) {
        try {
            this.logicDefineService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/logicDefine/all")
    @ApiOperation(value = "获取所有数据用途定义接口", notes = "获取所有数据用途定义接口")
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
