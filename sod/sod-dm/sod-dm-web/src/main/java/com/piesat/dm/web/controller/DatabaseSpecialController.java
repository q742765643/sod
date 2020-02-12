package com.piesat.dm.web.controller;

import com.netflix.discovery.converters.Auto;
import com.piesat.dm.entity.DatabaseSpecialReadWriteEntity;
import com.piesat.dm.rpc.api.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.dto.DatabaseUserDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
