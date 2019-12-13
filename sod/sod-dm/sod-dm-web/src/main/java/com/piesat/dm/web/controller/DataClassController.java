package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月29日 09:53:19
 */
@RestController
@Api(value = "资料分类controller", tags = {"资料分类操作接口"})
public class DataClassController {
    @Autowired
    private DataClassService dataClassService;

    @PostMapping(value = "/api/dataClass/save")
    @ApiOperation(value = "添加资料分类接口", notes = "添加资料分类接口")
    public ResultT save(@RequestBody DataClassDto dataClassDto) {
        try {
            DataClassDto save = this.dataClassService.saveDto(dataClassDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/dataClass/get")
    @ApiOperation(value = "获取资料分类接口", notes = "获取资料分类接口")
    public ResultT get(String id) {
        try {
            DataClassDto dataClassDto = this.dataClassService.getDotById(id);
            return ResultT.success(dataClassDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/dataClass/del")
    @ApiOperation(value = "删除资料分类接口", notes = "删除资料分类接口")
    public ResultT del(String id) {
        try {
            this.dataClassService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/dataClass/all")
    @ApiOperation(value = "获取所有资料分类接口", notes = "获取所有资料分类接口")
    public ResultT all() {
        try {
            List<DataClassDto> all = this.dataClassService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
