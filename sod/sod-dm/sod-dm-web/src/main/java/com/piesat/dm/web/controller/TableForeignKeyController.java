package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.TableForeignKeyService;
import com.piesat.dm.rpc.dto.TableForeignKeyDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:14:12
 */
@RestController
@Api(value="数据库外键关联controller",tags={"数据库外键关联接口"})
public class TableForeignKeyController {
    @Autowired
    private TableForeignKeyService tableForeignKeyService;

    @PostMapping(value = "/api/tableForeignKey/save")
    @ApiOperation(value = "添加数据库外键关联接口", notes = "添加数据库外键关联接口")
    public ResultT save(@RequestBody TableForeignKeyDto TableForeignKeyDto) {
        try {
            TableForeignKeyDto save = this.tableForeignKeyService.saveDto(TableForeignKeyDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/tableForeignKey/get")
    @ApiOperation(value = "获取数据库外键关联接口", notes = "获取数据库外键关联接口")
    public ResultT get(String id) {
        try {
            TableForeignKeyDto TableForeignKeyDto = this.tableForeignKeyService.getDotById(id);
            return ResultT.success(TableForeignKeyDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/tableForeignKey/del")
    @ApiOperation(value = "删除数据库外键关联接口", notes = "删除数据库外键关联接口")
    public ResultT del(String id) {
        try {
            this.tableForeignKeyService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/tableForeignKey/all")
    @ApiOperation(value = "获取所有数据库外键关联接口", notes = "获取所有数据库外键关联接口")
    public ResultT all() {
        try {
            List<TableForeignKeyDto> all = this.tableForeignKeyService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
