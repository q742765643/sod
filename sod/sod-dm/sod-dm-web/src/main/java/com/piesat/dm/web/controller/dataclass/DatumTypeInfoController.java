package com.piesat.dm.web.controller.dataclass;

import com.alibaba.fastjson.JSONArray;
import com.piesat.dm.rpc.api.dataclass.DatumTypeInfoService;
import com.piesat.dm.rpc.dto.dataclass.DatumTypeInfoDto;
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
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月29日 10:01:31
 */
@Api(tags = "公共元数据管理")
@RequestMapping("/dm/datumType")
@RestController
public class DatumTypeInfoController {
    @Autowired
    private DatumTypeInfoService datumTypeInfoService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:datumType:add")
    @Log(title = "公共元数据管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DatumTypeInfoDto datumTypeInfoDto) {
        try {
            DatumTypeInfoDto save = this.datumTypeInfoService.saveDto(datumTypeInfoDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:datumType:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DatumTypeInfoDto datumTypeInfoDto = this.datumTypeInfoService.getDotById(id);
            return ResultT.success(datumTypeInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:datumType:del")
    @Log(title = "公共元数据管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.datumTypeInfoService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:datumType:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DatumTypeInfoDto> all = this.datumTypeInfoService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询资料分类树")
    @RequiresPermissions("dm:datumType:getTree")
    @GetMapping(value = "/getTree")
    public ResultT getTree() {
        try {
            JSONArray tree = this.datumTypeInfoService.getTree();
            return ResultT.success(tree);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询资料分类树(简化)")
    @RequiresPermissions("dm:datumType:getSimpleTree")
    @GetMapping(value = "/getSimpleTree")
    public ResultT getSimpleTree() {
        try {
            JSONArray tree = this.datumTypeInfoService.getSimpleTree();
            return ResultT.success(tree);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
