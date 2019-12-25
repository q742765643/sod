package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.api.DataTableService;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月29日 09:56:25
 */
@RestController
@Api(value="表信息controller",tags={"表信息操作接口"})
public class DataTableController {
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataLogicService dataLogicService;

    @PostMapping(value = "/api/dataTable/save")
    @ApiOperation(value = "添加表信息接口", notes = "添加表信息接口")
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

    @PostMapping(value = "/api/dataTable/get")
    @ApiOperation(value = "获取表信息接口", notes = "获取表信息接口")
    public ResultT get(String id) {
        try {
            DataTableDto dataTableDto = this.dataTableService.getDotById(id);
            return ResultT.success(dataTableDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/dataTable/del")
    @ApiOperation(value = "删除表信息接口", notes = "删除表信息接口")
    public ResultT del(String id) {
        try {
            this.dataTableService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/dataTable/all")
    @ApiOperation(value = "获取所有表信息接口", notes = "获取所有表信息接口")
    public ResultT all() {
        try {
            List<DataTableDto> all = this.dataTableService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/dataTable/getByDatabaseId")
    @ApiOperation(value = "根据物理库id获取所有表信息接口", notes = "根据物理库id获取所有表信息接口")
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
