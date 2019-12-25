package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.api.DataTableService;
import com.piesat.dm.rpc.api.TableColumnService;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.TableColumnDto;
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
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月29日 10:27:31
 */
@Api(tags = "表字段管理")
@RequestMapping("/dm/tableColumn")
@RestController
public class TableColumnController {
    @Autowired
    private TableColumnService tableColumnService;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataLogicService dataLogicService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:tableColumn:add")
    @Log(title = "表字段管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody TableColumnDto tableColumnDto) {
        try {
            TableColumnDto save = this.tableColumnService.saveDto(tableColumnDto);
            DataTableDto dataTable = dataTableService.getDotById(save.getTableId());
            DataLogicDto dataLogic = dataTable.getClassLogicId();
            if (dataLogic.getIsComplete()==null||!dataLogic.getIsComplete()){
                dataLogic.setIsComplete(true);
                dataLogicService.saveDto(dataLogic);
            }
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:tableColumn:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            TableColumnDto tableColumnDto = this.tableColumnService.getDotById(id);
            return ResultT.success(tableColumnDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:tableColumn:del")
    @Log(title = "表字段管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.tableColumnService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:tableColumn:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<TableColumnDto> all = this.tableColumnService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
