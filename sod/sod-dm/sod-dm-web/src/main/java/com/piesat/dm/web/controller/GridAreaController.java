package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.GridAreaService;
import com.piesat.dm.rpc.dto.GridAreaDto;
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
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:44:21
 */
@Api(tags = "区域信息")
@RequestMapping("/dm/gridarea")
@RestController
public class GridAreaController {
    @Autowired
    private GridAreaService gridAreaService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:gridarea:add")
    @Log(title = "区域信息管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody GridAreaDto gridAreaDto) {
        try {
            GridAreaDto save = this.gridAreaService.saveDto(gridAreaDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:gridarea:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            GridAreaDto gridAreaDto = this.gridAreaService.getDotById(id);
            return ResultT.success(gridAreaDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:gridarea:del")
    @Log(title = "区域信息管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.gridAreaService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:gridarea:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<GridAreaDto> all = this.gridAreaService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
