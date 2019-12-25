package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.api.DataTableService;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
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
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月29日 09:56:25
 */
@Api(tags = "表信息管理")
@RequestMapping("/dm/dataTable")
@RestController
public class DataTableController {
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataLogicService dataLogicService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataTable:add")
    @Log(title = "表信息管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataTableDto dataTableDto) {
        try {
            DataLogicDto dataLogicDto = dataLogicService.getDotById(dataTableDto.getClassLogicId().getId());
            dataTableDto.setClassLogicId(dataLogicDto);
            DataTableDto save = this.dataTableService.saveDto(dataTableDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataTable:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataTableDto dataTableDto = this.dataTableService.getDotById(id);
            return ResultT.success(dataTableDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataTable:del")
    @Log(title = "表信息管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataTableService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataTable:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataTableDto> all = this.dataTableService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据物理库id获取所有表信息")
    @RequiresPermissions("dm:dataTable:databaseId")
    @GetMapping(value = "/getByDatabaseId")
    public ResultT getByDatabaseId(String databaseId){
        try {
            List<DataTableDto> all = this.dataTableService.getByDatabaseId(databaseId);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


}
