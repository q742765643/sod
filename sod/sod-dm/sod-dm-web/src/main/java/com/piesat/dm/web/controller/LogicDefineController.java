package com.piesat.dm.web.controller;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.util.ExportTableUtil;
import com.piesat.dm.entity.LogicStorageTypesEntity;
import com.piesat.dm.rpc.api.LogicDefineService;
import com.piesat.dm.rpc.dto.LogicDatabaseDto;
import com.piesat.dm.rpc.dto.LogicDefineDto;
import com.piesat.dm.rpc.dto.LogicStorageTypesDto;
import com.piesat.dm.rpc.service.GrpcService;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月25日 14:46:04
 */
@Api(tags = "数据用途定义")
@RequestMapping("/dm/logicDefine")
@RestController
public class LogicDefineController {
    @Autowired
    private LogicDefineService logicDefineService;
    @Autowired
    private GrpcService grpcService;
    @GrpcHthtClient
    private DictDataService dictDataService;


    @GetMapping("/list")
    @RequiresPermissions("dm:logicDefine:list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(LogicDefineDto logicDefineDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<LogicDefineDto> pageForm = new PageForm<>(pageNum, pageSize, logicDefineDto);
        PageBean pageBean = grpcService.selectLogicDefinePageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "查询所有表类型")
    @GetMapping(value = "/getAllStorageType")
    public ResultT getAllStorageType() {
        try {
            List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType("sys_storage_type");
            return ResultT.success(dictDataDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:logicDefine:add")
    @Log(title = "数据用途定义", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody LogicDefineDto logicDefineDto) {
        try {
            logicDefineDto = this.logicDefineService.saveDto(logicDefineDto);
            return ResultT.success(logicDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:logicDefine:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            LogicDefineDto logicDefineDto = this.logicDefineService.getDotById(id);
            return ResultT.success(logicDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:logicDefine:del")
    @Log(title = "数据用途定义", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.logicDefineService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:logicDefine:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<LogicDefineDto> allLogicDefine = this.logicDefineService.getAllLogicDefine();
            return ResultT.success(allLogicDefine);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/exportTable")
    @ApiOperation(value = "导出", notes = "导出")
    public void exportTable(LogicDefineDto logicDefineDto, HttpServletRequest request, HttpServletResponse response){
        List<LogicDefineDto> logicDefineDtos = logicDefineService.findByParam(logicDefineDto);
        //定义存放列名的集合
        ArrayList<String> headList = new ArrayList<>();
        headList.add("逻辑库ID");
        headList.add("逻辑库名称");
        headList.add("表类型");
        headList.add("用途说明");
        headList.add("物理库名称");
        headList.add("创建时间");
        //把对象转为list存储
        ArrayList<List<String>> lists = new ArrayList<>();
        for (LogicDefineDto logic : logicDefineDtos) {
            StringBuilder logicStorageTypes = new StringBuilder();
            StringBuilder logicDatabases = new StringBuilder();
            for(LogicStorageTypesDto logicStorageTypesDto:logic.getLogicStorageTypesEntityList()){
                if(StringUtils.isNotNullString(logicStorageTypes.toString())){
                    logicStorageTypes.append(",").append(logicStorageTypesDto.getStorageType());
                }else{
                    logicStorageTypes.append(logicStorageTypesDto.getStorageType());
                }
            }
            for(LogicDatabaseDto logicDatabaseDto:logic.getLogicDatabaseEntityList()){
                if(StringUtils.isNotNullString(logicDatabases.toString())){
                    logicDatabases.append(",").append(logicDatabaseDto.getDatabaseId());
                }else{
                    logicDatabases.append(logicDatabaseDto.getDatabaseId());
                }
            }
            ArrayList<String> strings = new ArrayList<>();
            strings.add(logic.getLogicFlag());
            strings.add(logic.getLogicName());
            strings.add(logicStorageTypes.toString());
            strings.add(logic.getLogicDesc());
            strings.add(logicDatabases.toString());
            strings.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(logic.getCreateTime()));
            lists.add(strings);
        }
        ExportTableUtil.exportTable(request, response, headList, lists , "逻辑库定义导出");
    }

    @ApiOperation(value = "查询所有(升级版)")
    @RequiresPermissions("dm:logicDefine:getAllLd")
    @GetMapping(value = "/getAllLd")
    public ResultT getAllLd() {
        try {
            List<LogicDefineDto> allLogicDefine = this.grpcService.getAllLogicDefine();
            return ResultT.success(allLogicDefine);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
