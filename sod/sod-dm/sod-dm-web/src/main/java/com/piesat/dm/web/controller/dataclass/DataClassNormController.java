package com.piesat.dm.web.controller.dataclass;

import com.piesat.dm.rpc.api.dataclass.DataClassNormService;
import com.piesat.dm.rpc.dto.dataclass.DataClassNormDto;
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
 * 规范信息
 *
 * @author cwh
 * @date 2020年 02月12日 17:04:29
 */
@Api(tags = "规范信息")
@RequestMapping("/dm/dataclassnorm")
@RestController
public class DataClassNormController {
    @Autowired
    private DataClassNormService dataClassNormService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataclassnorm:add")
    @Log(title = "规范信息管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataClassNormDto dataClassNormDto) {
        try {
            DataClassNormDto save = this.dataClassNormService.saveDto(dataClassNormDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataclassnorm:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataClassNormDto dataClassNormDto = this.dataClassNormService.getDotById(id);
            return ResultT.success(dataClassNormDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataclassnorm:del")
    @Log(title = "规范信息管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataClassNormService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataclassnorm:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataClassNormDto> all = this.dataClassNormService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
