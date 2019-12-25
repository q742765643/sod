package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.dto.DataLogicDto;
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
 * 数据用途
 *
 * @author cwh
 * @date 2019年 11月25日 14:18:09
 */
@Api(tags = "数据用途管理")
@RequestMapping("/dm/dataLogic")
@RestController
public class DataLogicController {
    @Autowired
    private DataLogicService dataLogicService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataLogic:add")
    @Log(title = "数据用途管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save (@RequestBody DataLogicDto dataLogicDto){
        try {
            dataLogicDto = this.dataLogicService.saveDto(dataLogicDto);
            return ResultT.success(dataLogicDto);
        }catch (Exception e){
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataLogic:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataLogicDto dataLogicDto = this.dataLogicService.getDotById(id);
            return ResultT.success(dataLogicDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataLogic:del")
    @Log(title = "数据用途管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataLogicService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataLogic:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataLogicDto> all = this.dataLogicService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
