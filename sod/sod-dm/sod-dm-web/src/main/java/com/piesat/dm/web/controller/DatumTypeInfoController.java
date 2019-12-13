package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DatumTypeInfoService;
import com.piesat.dm.rpc.dto.DatumTypeInfoDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月29日 10:01:31
 */
@RestController
@Api(value="公共元数据controller",tags={"公共元数据操作接口"})
public class DatumTypeInfoController {
    @Autowired
    private DatumTypeInfoService datumTypeInfoService;

    @PostMapping(value = "/api/datumTypeInfo/save")
    @ApiOperation(value = "添加公共元数据接口", notes = "添加公共元数据接口")
    public ResultT save(@RequestBody DatumTypeInfoDto datumTypeInfoDto) {
        try {
            DatumTypeInfoDto save = this.datumTypeInfoService.saveDto(datumTypeInfoDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/datumTypeInfo/get")
    @ApiOperation(value = "获取公共元数据接口", notes = "获取公共元数据接口")
    public ResultT get(String id) {
        try {
            DatumTypeInfoDto datumTypeInfoDto = this.datumTypeInfoService.getDotById(id);
            return ResultT.success(datumTypeInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/datumTypeInfo/del")
    @ApiOperation(value = "删除公共元数据接口", notes = "删除公共元数据接口")
    public ResultT del(String id) {
        try {
            this.datumTypeInfoService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/datumTypeInfo/all")
    @ApiOperation(value = "获取所有公共元数据接口", notes = "获取所有公共元数据接口")
    public ResultT all() {
        try {
            List<DatumTypeInfoDto> all = this.datumTypeInfoService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
