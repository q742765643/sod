package com.piesat.dm.web.controller.dataclass;

import com.piesat.dm.rpc.api.dataclass.DataClassLabelDefService;
import com.piesat.dm.rpc.dto.dataclass.DataClassLabelDefDto;
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
 * @author cwh
 * @program: sod
 * @description: 数据标签定义
 * @date 2021年 03月09日 14:46:49
 */
@Api(tags = "资料标签定义")
@RequestMapping("/dm/dataClassLabelDef")
@RestController
public class DataClassLabelDefController {
    @Autowired
    private DataClassLabelDefService DataClassLabelDefService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:DataClassLabelDef:add")
    @Log(title = "资料标签定义", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataClassLabelDefDto DataClassLabelDefDto) {
        try {
            DataClassLabelDefDto save = this.DataClassLabelDefService.saveDto(DataClassLabelDefDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:DataClassLabelDef:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataClassLabelDefDto DataClassLabelDefDto = this.DataClassLabelDefService.getDotById(id);
            return ResultT.success(DataClassLabelDefDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:DataClassLabelDef:del")
    @Log(title = "资料标签定义", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.DataClassLabelDefService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:DataClassLabelDef:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataClassLabelDefDto> all = this.DataClassLabelDefService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
