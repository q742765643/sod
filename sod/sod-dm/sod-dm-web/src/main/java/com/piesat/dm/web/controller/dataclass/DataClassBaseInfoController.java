package com.piesat.dm.web.controller.dataclass;

import com.piesat.dm.rpc.api.dataclass.DataClassBaseInfoService;
import com.piesat.dm.rpc.dto.dataclass.DataClassBaseInfoDto;
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
 * 资料基础信息
 *
 * @author cwh
 * @date 2020年 04月02日 09:29:38
 */
@Api(tags = "资料基础信息")
@RequestMapping("/dm/classbaseinfo")
@RestController
public class DataClassBaseInfoController {
    @Autowired
    private DataClassBaseInfoService cataClassBaseInfoService;



    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:classbaseinfo:add")
    @Log(title = "资料基础信息", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataClassBaseInfoDto dataClassBaseInfoDto) {
        try {
            DataClassBaseInfoDto save = this.cataClassBaseInfoService.saveDto(dataClassBaseInfoDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:classbaseinfo:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataClassBaseInfoDto dataClassBaseInfoDto = this.cataClassBaseInfoService.getDotById(id);
            return ResultT.success(dataClassBaseInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:classbaseinfo:del")
    @Log(title = "资料基础信息", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.cataClassBaseInfoService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:classbaseinfo:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataClassBaseInfoDto> all = this.cataClassBaseInfoService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据存储编码查询基础信息")
    @RequiresPermissions("dm:classbaseinfo:getDataClassBaseInfo")
    @GetMapping(value = "/getDataClassBaseInfo")
    public ResultT getDataClassBaseInfo(String id) {
        try {
            DataClassBaseInfoDto all = this.cataClassBaseInfoService.getDataClassBaseInfo(id);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据存储编码查询所有基础信息")
    @RequiresPermissions("dm:classbaseinfo:getAllDataClassBaseInfo")
    @GetMapping(value = "/getAllDataClassBaseInfo")
    public ResultT getAllDataClassBaseInfo(String id) {
        try {
            DataClassBaseInfoDto all = this.cataClassBaseInfoService.getAllDataClassBaseInfo(id);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "保存")
    @RequiresPermissions("dm:classbaseinfo:saveDataClassBaseInfo")
    @Log(title = "资料基础信息", businessType = BusinessType.INSERT)
    @PostMapping(value = "/saveDataClassBaseInfo")
    public ResultT saveDataClassBaseInfo(@RequestBody DataClassBaseInfoDto dataClassBaseInfoDto) {
        try {
            DataClassBaseInfoDto save = this.cataClassBaseInfoService.saveDataClassBaseInfo(dataClassBaseInfoDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
