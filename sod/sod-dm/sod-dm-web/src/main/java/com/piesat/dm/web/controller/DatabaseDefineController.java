package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DatabaseDefineService;
import com.piesat.dm.rpc.dto.DatabaseDefineDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月29日 09:41:21
 */
@Api(tags = "数据库类型管理")
@RequestMapping("/dm/databaseDefine")
@RestController
public class DatabaseDefineController {
    @Autowired
    private DatabaseDefineService databaseDefineService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:databaseDefine:add")
    @Log(title = "数据库类型管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DatabaseDefineDto databaseDefineDto) {
        try {
            DatabaseDefineDto save = this.databaseDefineService.saveDto(databaseDefineDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:databaseDefine:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DatabaseDefineDto DatabaseDefineDto = this.databaseDefineService.getDotById(id);
            return ResultT.success(DatabaseDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:databaseDefine:del")
    @Log(title = "数据库类型管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.databaseDefineService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:databaseDefine:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DatabaseDefineDto> all = this.databaseDefineService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "分页查询(支持id和databaseName查询)")
    @RequiresPermissions("dm:databaseDefine:page")
    @GetMapping(value = "/page")
    public ResultT<PageBean> getPage(DatabaseDefineDto databaseDefineDto,
                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            PageBean page = this.databaseDefineService.getPage(databaseDefineDto, pageNum, pageSize);
            return ResultT.success(page);
        }catch (Exception e){
            return ResultT.failed(e.getMessage());
        }
    }
}
