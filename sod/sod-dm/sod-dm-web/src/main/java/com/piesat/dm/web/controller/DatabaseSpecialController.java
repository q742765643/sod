package com.piesat.dm.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.netflix.discovery.converters.Auto;
import com.piesat.dm.entity.DatabaseSpecialReadWriteEntity;
import com.piesat.dm.rpc.api.DatabaseSpecialAuthorityService;
import com.piesat.dm.rpc.api.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.*;
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
 * 专题库管理
 * @Description 
 * @ClassName DatabaseSpecialController
 * @author wulei
 * @date 2020/2/12 15:56
 */
@Api(tags = "专题库管理")
@RequestMapping("/dm/databaseSpecial")
@RestController
public class DatabaseSpecialController {

    @Autowired
    private DatabaseSpecialService databaseSpecialService;
    @Autowired
    private DatabaseSpecialReadWriteService databaseSpecialReadWriteService;
    @Autowired
    private DatabaseSpecialAuthorityService databaseSpecialAuthorityService;

    @ApiOperation(value = "获取专题库列表")
    @RequiresPermissions("dm:databaseSpecial:specialList")
    @GetMapping(value = "/specialList")
    public ResultT all() {
        try {
            List<DatabaseSpecialDto> all = this.databaseSpecialService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:databaseSpecial:getById")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            DatabaseSpecialDto databaseSpecialDto = this.databaseSpecialService.getDotById(id);
            return ResultT.success(databaseSpecialDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取专题库资料列表")
    @RequiresPermissions("dm:databaseSpecial:getSpecialDataList")
    @GetMapping(value = "/getSpecialDataList")
    public ResultT getSpecialDataList(String sdbId,String dataType) {
        try {
            List<DatabaseSpecialReadWriteDto> dataList = this.databaseSpecialReadWriteService.getDotList(sdbId,dataType);
            return ResultT.success(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:databaseSpecial:del")
    @DeleteMapping(value = "/delete")
    public ResultT delete(String id) {
        try {
            this.databaseSpecialService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "修改专题库基本信息")
    @RequiresPermissions("dm:databaseSpecial:update")
    @PostMapping(value = "/update")
    public ResultT update(@RequestBody DatabaseSpecialDto databaseSpecialDto) {
        try {
            DatabaseSpecialDto save = this.databaseSpecialService.saveDto(databaseSpecialDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据获取专题库对应数据库权限")
    @RequiresPermissions("dm:databaseSpecial:getAuthorityBySdbId")
    @GetMapping(value = "/getAuthorityBySdbId")
    public ResultT getAuthorityBySdbId(String sdbId) {
        try {
            List<DatabaseSpecialAuthorityDto> authorityList = this.databaseSpecialAuthorityService.getAuthorityBySdbId(sdbId);
            return ResultT.success(authorityList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "数据库授权")
    @RequiresPermissions("dm:databaseSpecial:empowerDatabaseSperial")
    @PostMapping(value = "/empowerDatabaseSperial")
    public ResultT empowerDatabaseSperial(@RequestBody DatabaseDto DatabaseDto) {
        try {
            this.databaseSpecialService.empowerDatabaseSperial(DatabaseDto);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "资料授权-单独")
    @RequiresPermissions("dm:databaseSpecial:empowerDataOne")
    @PostMapping(value = "/empowerDataOne")
    public ResultT empowerDataOne(@RequestBody DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        try {
            this.databaseSpecialService.empowerDataOne(databaseSpecialReadWriteDto);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "资料授权-批量")
    @RequiresPermissions("dm:databaseSpecial:empowerDataOne")
    @PostMapping(value = "/empowerDataBatch")
    public ResultT empowerDataBatch(@RequestBody String listString) {
        try {
            List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtoList = JSONObject.parseArray(listString,
                    DatabaseSpecialReadWriteDto.class);
            this.databaseSpecialService.empowerDataBatch(databaseSpecialReadWriteDtoList);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
