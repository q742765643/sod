package com.piesat.dm.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.piesat.dm.core.parser.DatabaseType;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.dm.rpc.dto.DatabaseDefineDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月29日 09:53:19
 */
@Api(tags = "资料分类管理")
@RequestMapping("/dm/dataClass")
@RestController
public class DataClassController {
    @Autowired
    private DataClassService dataClassService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataClass:add")
    @Log(title = "资料分类管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataClassDto dataClassDto) {
        try {
            DataClassDto save = this.dataClassService.saveDto(dataClassDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataClass:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataClassDto dataClassDto = this.dataClassService.getDotById(id);
            return ResultT.success(dataClassDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据DataClassId查询")
    @RequiresPermissions("dm:dataClass:findByClassId")
    @GetMapping(value = "/findByClassId")
    public ResultT findByClassId(String id) {
        try {
            DataClassDto dataClassDto = this.dataClassService.findByDataClassId(id);
            return ResultT.success(dataClassDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataClass:del")
    @Log(title = "资料分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataClassService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据dataClassId删除")
    @RequiresPermissions("dm:dataClass:delByClass")
    @Log(title = "资料分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/delByClass")
    public ResultT deleteByClassName(String dataClassId){
        try {
            this.dataClassService.deleteByDataClassId(dataClassId);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataClass:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataClassDto> all = this.dataClassService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "按数据用途查询资料分类")
    @RequiresPermissions("dm:dataClass:logicClass")
    @GetMapping(value = "/logicClass")
    public ResultT getLogicClass() {
        try {
            JSONArray all = this.dataClassService.getLogicClass();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "按数据库查询资料分类")
    @RequiresPermissions("dm:dataClass:databaseClass")
    @GetMapping(value = "/databaseClass")
    public ResultT getDatabaseClass() {
        try {
            JSONArray all = null;
            if (DatabaseType.databaseType.toLowerCase().equals("mysql")){
                all = this.dataClassService.getDatabaseClassMysql();
            }else {
                all = this.dataClassService.getDatabaseClass();
            }

            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询资料分类树")
    @RequiresPermissions("dm:dataClass:getTree")
    @GetMapping(value = "/getTree")
    public ResultT getTree() {
        try {
            JSONArray tree = this.dataClassService.getTree();
            return ResultT.success(tree);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据目录查询资料")
    @RequiresPermissions("dm:dataClass:getListBYIn")
    @GetMapping(value = "/getListBYIn")
    public ResultT getListBYIn(List<String> classIds, String className, String dDataId){
        try {
            List<Map<String, Object>> all = this.dataClassService.getListBYIn(classIds,className,dDataId);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询资料概览")
    @RequiresPermissions("dm:dataClass:getBaseData")
    @GetMapping(value = "/getBaseData")
    public ResultT getBaseData(DataClassDto dataClassDto,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        try {
            PageBean all = this.dataClassService.getBaseData(new PageForm(pageNum, pageSize),dataClassDto);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
