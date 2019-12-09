package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.TableIndexService;
import com.piesat.dm.rpc.dto.TableIndexDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 表索引
 *
 * @author cwh
 * @date 2019年 11月29日 10:30:01
 */
@RestController
@Api(value="表索引信息controller",tags={"表索引信息接口"})
public class TableIndexController {
    @Autowired
    private TableIndexService tableIndexService;

    @PostMapping(value = "/api/tableIndex/save")
    @ApiOperation(value = "添加表索引信息接口", notes = "添加表索引信息接口")
    public ResultT save(@RequestBody TableIndexDto tableIndexDto) {
        try {
            TableIndexDto save = this.tableIndexService.saveDto(tableIndexDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/tableIndex/get")
    @ApiOperation(value = "获取表索引信息接口", notes = "获取表索引信息接口")
    public ResultT get(String id) {
        try {
            TableIndexDto tableIndexDto = this.tableIndexService.getDotById(id);
            return ResultT.success(tableIndexDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/tableIndex/del")
    @ApiOperation(value = "删除表索引信息接口", notes = "删除表索引信息接口")
    public ResultT del(String id) {
        try {
            this.tableIndexService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/tableIndex/all")
    @ApiOperation(value = "获取所有表索引信息接口", notes = "获取所有表索引信息接口")
    public ResultT all() {
        try {
            List<TableIndexDto> all = this.tableIndexService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
