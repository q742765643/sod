package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.api.DataTableService;
import com.piesat.dm.rpc.api.TableColumnService;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.TableColumnDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月29日 10:27:31
 */
@RestController
@Api(value="表字段信息controller",tags={"表字段信息接口"})
public class TableColumnController {
    @Autowired
    private TableColumnService tableColumnService;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataLogicService dataLogicService;

    @PostMapping(value = "/api/tableColumn/save")
    @ApiOperation(value = "添加表字段信息接口", notes = "添加表字段信息接口")
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

    @PostMapping(value = "/api/tableColumn/get")
    @ApiOperation(value = "获取表字段信息接口", notes = "获取表字段信息接口")
    public ResultT get(String id) {
        try {
            TableColumnDto tableColumnDto = this.tableColumnService.getDotById(id);
            return ResultT.success(tableColumnDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/tableColumn/del")
    @ApiOperation(value = "删除表字段信息接口", notes = "删除表字段信息接口")
    public ResultT del(String id) {
        try {
            this.tableColumnService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/tableColumn/all")
    @ApiOperation(value = "获取所有表字段信息接口", notes = "获取所有表字段信息接口")
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
