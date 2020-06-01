package com.piesat.dm.web.controller.datatable;

import com.piesat.dm.rpc.api.datatable.DataServerBaseInfoService;
import com.piesat.dm.rpc.dto.datatable.DataServerBaseInfoDto;
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
 * 服务基础信息
 *
 * @author cwh
 * @date 2020年 02月12日 15:20:53
 */
@Api(tags = "服务基础信息")
@RequestMapping("/dm/dataserverbaseinfo")
@RestController
public class DataServerBaseInfoController {
    @Autowired
    private DataServerBaseInfoService dataServerBaseInfoService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataserverbaseinfo:add")
    @Log(title = "服务基础信息管理（新增）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataServerBaseInfoDto dataServerBaseInfoDto) {
        try {
            DataServerBaseInfoDto save = this.dataServerBaseInfoService.saveDto(dataServerBaseInfoDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataserverbaseinfo:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataServerBaseInfoDto dataServerBaseInfoDto = this.dataServerBaseInfoService.getDotById(id);
            return ResultT.success(dataServerBaseInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataserverbaseinfo:del")
    @Log(title = "服务基础信息管理（删除）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataServerBaseInfoService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataserverbaseinfo:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataServerBaseInfoDto> all = this.dataServerBaseInfoService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据存储编码查询")
    @RequiresPermissions("dm:dataserverbaseinfo:findByDataCLassId")
    @GetMapping(value = "/findByDataCLassId")
    public ResultT findByDataCLassId(String dataCLassId) {
        try {
            List<DataServerBaseInfoDto> all = this.dataServerBaseInfoService.findByDataCLassId(dataCLassId);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
