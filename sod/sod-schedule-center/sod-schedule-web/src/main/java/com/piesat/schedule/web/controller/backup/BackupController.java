package com.piesat.schedule.web.controller.backup;

import com.piesat.common.utils.OwnException;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-23 11:37
 **/
@RestController
@Api(value="数据备份接口",tags = {"数据备份接口"})
@RequestMapping("/schedule/backup")
public class BackupController {
    @Autowired
    private BackupService backupService;

    @RequiresPermissions("schedule:backup:list")
    @ApiOperation(value = "分页查询备份任务", notes = "分页查询备份任务")
    @GetMapping("/list")
    public ResultT<PageBean<BackUpDto>> list(BackUpDto backup, int pageNum, int pageSize)
    {
        ResultT<PageBean<BackUpDto>> resultT=new ResultT<>();
        PageForm<BackUpDto> pageForm=new PageForm<>(pageNum,pageSize,backup);
        PageBean pageBean=backupService.selectBackupList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:backup:query")
    @ApiOperation(value = "根据ID查询备份任务", notes = "根据ID查询备份任务")
    @GetMapping(value = "/{backupId}")
    public ResultT<BackUpDto> getInfo(@PathVariable String backupId)
    {
        ResultT<BackUpDto> resultT=new ResultT<>();
        BackUpDto backUpDto= backupService.findBackupById(backupId);
        resultT.setData(backUpDto);
        return resultT;
    }
    @RequiresPermissions("schedule:backup:add")
    @ApiOperation(value = "添加备份任务", notes = "添加备份任务")
    @Log(title = "备份任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody BackUpDto backup)
    {
        if(null==backup.getIsAlarm()||"".equals(backup.getIsAlarm())){
            backup.setIsAlarm("1");
        }
        ResultT<String> resultT=new ResultT<>();
        backupService.saveBackup(backup);
        return resultT;
    }

    @RequiresPermissions("schedule:backup:edit")
    @ApiOperation(value = "修改备份任务", notes = "修改备份任务")
    @Log(title = "备份任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody BackUpDto backup)
    {
        if(null==backup.getIsAlarm()||"".equals(backup.getIsAlarm())){
            backup.setIsAlarm("1");
        }
        ResultT<String> resultT=new ResultT<>();
        backupService.updateBackup(backup);
        return resultT;
    }
    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:backup:remove")
    @ApiOperation(value = "删除备份任务", notes = "删除备份任务")
    @Log(title = "备份任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{backupIds}")
    public  ResultT<String> remove(@PathVariable String[] backupIds)
    {
        ResultT<String> resultT=new ResultT<>();
        backupService.deleteBackupByIds(backupIds);
        return resultT;
    }
    @ApiOperation(value = "查询备份任务物理库", notes = "查询备份任务物理库")
    @GetMapping("/findDatabase")
    public ResultT findDatabase(){
        ResultT resultT=new ResultT<>();
        List<Map<String,Object>> mapList=backupService.findDatabase();
        resultT.setData(mapList);
        return resultT;
    }
    @ApiOperation(value = "查询需要备份资料列表", notes = "查询需要备份资料列表")
    @GetMapping("/findDataClassId")
    public ResultT findDataClassId(String databaseId,String dataClassId){
        ResultT resultT=new ResultT<>();
        List<Map<String,Object>> mapList=backupService.findDataClassId(databaseId,dataClassId);
        resultT.setData(mapList);
        return resultT;
    }
    @ApiOperation(value = "数据备份配置导出", notes = "数据备份配置导出")
    @RequiresPermissions("schedule:backup:export")
    @GetMapping("/export")
    public void exportExcel(BackUpDto backUpDto){
         backupService.exportExcel(backUpDto);

    }
    @GetMapping(value = "/execute")
    @RequiresPermissions("schedule:job:execute")
    @ApiOperation(value = "立即执行接口", notes = "立即执行接口")
    public ResultT<String> execute(String id){
        return backupService.execute(id);
    }
}

