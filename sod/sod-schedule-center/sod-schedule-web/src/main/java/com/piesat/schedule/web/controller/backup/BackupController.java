package com.piesat.schedule.web.controller.backup;

import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-23 11:37
 **/
@RestController
@RequestMapping("/schedule/backup")
public class BackupController {
    @Autowired
    private BackupService backupService;

    @RequiresPermissions("schedule:backup:list")
    @GetMapping("/list")
    public ResultT<PageBean> list(BackUpDto backup, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<BackUpDto> pageForm=new PageForm<>(pageNum,pageSize,backup);
        PageBean pageBean=backupService.selectBackupList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:backup:query")
    @GetMapping(value = "/{backupId}")
    public ResultT<BackUpDto> getInfo(@PathVariable String backupId)
    {
        ResultT<BackUpDto> resultT=new ResultT<>();
        BackUpDto backUpDto= backupService.findBackupById(backupId);
        resultT.setData(backUpDto);
        return resultT;
    }
    @RequiresPermissions("schedule:backup:add")
    @Log(title = "备份任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody BackUpDto backup)
    {
        ResultT<String> resultT=new ResultT<>();
        backupService.saveBackup(backup);
        return resultT;
    }

    @RequiresPermissions("schedule:backup:edit")
    @Log(title = "备份任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody BackUpDto backup)
    {
        ResultT<String> resultT=new ResultT<>();
        backupService.updateBackup(backup);
        return resultT;
    }
    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:backup:remove")
    @Log(title = "备份任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{backupIds}")
    public  ResultT<String> remove(@PathVariable String[] backupIds)
    {
        ResultT<String> resultT=new ResultT<>();
        backupService.deleteBackupByIds(backupIds);
        return resultT;
    }
}

