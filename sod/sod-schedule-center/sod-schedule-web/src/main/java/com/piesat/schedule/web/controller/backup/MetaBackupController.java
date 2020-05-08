package com.piesat.schedule.web.controller.backup;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.rpc.api.backup.MetaBackupService;
import com.piesat.schedule.rpc.dto.backup.MetaBackupDto;
import com.piesat.schedule.rpc.dto.backup.MetaBackupLogDto;
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
 * @create: 2020-02-26 16:54
 **/
@Api(value="元数据备份接口",tags = {"元数据备份接口"})
@RestController
@RequestMapping("/schedule/metaBackup")
public class MetaBackupController {
    @Autowired
    private MetaBackupService metaBackupService;

    @RequiresPermissions("schedule:metaBackup:list")
    @ApiOperation(value = "分页查询元数据备份任务", notes = "分页查询元数据备份任务")
    @GetMapping("/list")
    public ResultT<PageBean> list(MetaBackupDto metaBackupDto, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<MetaBackupDto> pageForm=new PageForm<>(pageNum,pageSize,metaBackupDto);
        PageBean pageBean=metaBackupService.selectBackupList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }
    @RequiresPermissions("schedule:metaBackup:query")
    @ApiOperation(value = "根据ID查询元数据备份任务", notes = "根据ID查询元数据备份任务")
    @GetMapping(value = "/{metaBackupId}")
    public ResultT<MetaBackupDto> getInfo(@PathVariable String metaBackupId)
    {
        ResultT<MetaBackupDto> resultT=new ResultT<>();
        MetaBackupDto metaBackupDto= metaBackupService.findBackupById(metaBackupId);
        resultT.setData(metaBackupDto);
        return resultT;
    }

    @RequiresPermissions("schedule:metaBackup:add")
    @ApiOperation(value = "添加元数据备份任务", notes = "添加元数据备份任务")
    @Log(title = "元数据备份任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody MetaBackupDto metaBackupDto)
    {
        ResultT<String> resultT=new ResultT<>();
        metaBackupService.saveBackup(metaBackupDto);
        return resultT;
    }

    @RequiresPermissions("schedule:metaBackup:edit")
    @ApiOperation(value = "修改元数据备份任务", notes = "修改元数据备份任务")
    @Log(title = "元数据备份任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody MetaBackupDto metaBackupDto)
    {
        ResultT<String> resultT=new ResultT<>();
        metaBackupService.updateBackup(metaBackupDto);
        return resultT;
    }
    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:metaBackup:remove")
    @ApiOperation(value = "删除元数据备份任务", notes = "删除元数据备份任务")
    @Log(title = "元数据备份任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{metaBackupIds}")
    public  ResultT<String> remove(@PathVariable String[] metaBackupIds)
    {
        ResultT<String> resultT=new ResultT<>();
        metaBackupService.deleteBackupByIds(metaBackupIds);
        return resultT;
    }

    @RequiresPermissions("schedule:metaBackup:findMeta")
    @ApiOperation(value = "查询数据库元数据", notes = "查询数据库元数据")
    @GetMapping(value = "/findMeta")
    public ResultT findMeta(String databaseId)
    {
        ResultT resultT=new ResultT<>();
        List<TreeVo> treeVos= metaBackupService.findMeta(databaseId);
        resultT.setData(treeVos);
        return resultT;
    }
    @RequiresPermissions("schedule:metaBackup:findDataBase")
    @ApiOperation(value = "查询物理库IP", notes = "查询物理库IP")
    @GetMapping(value = "/findDataBase")
    public ResultT<List<Map<String,String>>> findDataBase(){
        ResultT<List<Map<String,String>>> resultT=new ResultT<>();
        List<Map<String,String>> mapList=metaBackupService.findDataBase();
        resultT.setData(mapList);
        return resultT;
    }
    @RequiresPermissions("schedule:metaBackup:handExecute")
    @ApiOperation(value = "手工执行元数据备份", notes = "手工执行元数据备份")
    @GetMapping(value = "/handExecute")
    public ResultT<String> handExecute(MetaBackupDto metaBackupDto){
        ResultT<String> resultT=new ResultT<>();
        metaBackupService.handExecute(metaBackupDto);
        return resultT;
    }
    @ApiOperation(value = "元数据备份配置导出", notes = "元数据备份配置导出")
    @RequiresPermissions("schedule:metaBackup:export")
    @GetMapping("/export")
    public void exportExcel(MetaBackupDto metaBackupDto){
        metaBackupService.exportExcel(metaBackupDto);
    }


}

