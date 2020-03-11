package com.piesat.schedule.web.controller.clear;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.rpc.api.clear.MetaClearService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.MetaBackupDto;
import com.piesat.schedule.rpc.dto.clear.MetaClearDto;
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
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 15:38
 **/
@RestController
@Api(value="元数据清除接口",tags = {"元数据清除接口"})
@RequestMapping("/schedule/metaClear")
public class MetaClearController {
    @Autowired
    private MetaClearService metaClearService;

    @RequiresPermissions("schedule:metaClear:list")
    @ApiOperation(value = "分页查询元数据清除任务", notes = "分页查询元数据清除任务")
    @GetMapping("/list")
    public ResultT<PageBean<MetaClearDto>> list(MetaClearDto metaClearDto, int pageNum, int pageSize)
    {
        ResultT<PageBean<MetaClearDto>> resultT=new ResultT<>();
        PageForm<MetaClearDto> pageForm=new PageForm<>(pageNum,pageSize,metaClearDto);
        PageBean<MetaClearDto> pageBean=metaClearService.selectMetaClearList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:metaClear:query")
    @ApiOperation(value = "根据ID查询元数据清除任务", notes = "根据ID查询元数据清除任务")
    @GetMapping(value = "/{metaClearId}")
    public ResultT<MetaClearDto> getInfo(@PathVariable String metaClearId)
    {
        ResultT<MetaClearDto> resultT=new ResultT<>();
        MetaClearDto metaClearDto= metaClearService.findMetaClearById(metaClearId);
        resultT.setData(metaClearDto);
        return resultT;
    }

    @RequiresPermissions("schedule:metaClear:add")
    @ApiOperation(value = "添加元数据清除任务", notes = "添加元数据清除任务")
    @Log(title = "元数据清除任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody MetaClearDto metaClearDto)
    {
        ResultT<String> resultT=new ResultT<>();
        metaClearService.saveMetaClear(metaClearDto);
        return resultT;
    }

    @RequiresPermissions("schedule:metaClear:edit")
    @ApiOperation(value = "修改元数据清除任务", notes = "修改元数据清除任务")
    @Log(title = "元数据清除任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody MetaClearDto metaClearDto)
    {
        ResultT<String> resultT=new ResultT<>();
        metaClearService.updateMetaClear(metaClearDto);
        return resultT;
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:metaClear:remove")
    @ApiOperation(value = "删除元数据清除任务", notes = "删除元数据清除任务")
    @Log(title = "元数据清除任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{metaClearIds}")
    public  ResultT<String> remove(@PathVariable String[] metaClearIds)
    {
        ResultT<String> resultT=new ResultT<>();
        metaClearService.deleteMeteClearByIds(metaClearIds);
        return resultT;
    }

    @RequiresPermissions("schedule:metaClear:findMeta")
    @ApiOperation(value = "查询数据库元数据", notes = "查询数据库元数据")
    @GetMapping(value = "/findMeta")
    public ResultT findMeta(String databaseId)
    {
        ResultT resultT=new ResultT<>();
        List<TreeVo> treeVos= metaClearService.findAllTableByIp(databaseId);
        resultT.setData(treeVos);
        return resultT;
    }
    @RequiresPermissions("schedule:metaClear:findDataBase")
    @ApiOperation(value = "查询物理库IP", notes = "查询物理库IP")
    @GetMapping(value = "/findDataBase")
    public ResultT<List<Map<String,String>>> findDataBase(){
        ResultT<List<Map<String,String>>> resultT=new ResultT<>();
        List<Map<String,String>> mapList=metaClearService.findDataBase();
        resultT.setData(mapList);
        return resultT;
    }

    @ApiOperation(value = "元数据清除配置导出", notes = "元数据清除配置导出")
    @RequiresPermissions("schedule:metaClear:export")
    @GetMapping("/export")
    public void exportExcel(MetaClearDto metaClearDto){
        metaClearService.exportExcel(metaClearDto);

    }
}

