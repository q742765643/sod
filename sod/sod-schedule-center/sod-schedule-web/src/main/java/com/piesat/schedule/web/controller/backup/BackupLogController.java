package com.piesat.schedule.web.controller.backup;

import com.piesat.schedule.rpc.api.backup.BackupLogService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-29 20:12
 **/
@RestController
@RequestMapping("/schedule/backupLog")
public class BackupLogController {
    @Autowired
    private BackupLogService backupLogService;

    @RequiresPermissions("schedule:backupLog:list")
    @GetMapping("/list")
    public ResultT<PageBean> list(BackupLogDto backupLog, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<BackupLogDto> pageForm=new PageForm<>(pageNum,pageSize,backupLog);
        PageBean pageBean=backupLogService.selectBackupLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:backupLog:query")
    @GetMapping(value = "/{backupLogId}")
    public ResultT<BackupLogDto> getInfo(@PathVariable String backupLogId)
    {
        ResultT<BackupLogDto> resultT=new ResultT<>();
        BackupLogDto backupLogDto= backupLogService.findBackupLogById(backupLogId);
        resultT.setData(backupLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:backupLog:remove")
    @Log(title = "备份任务日志管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{backupLogIds}")
    public  ResultT<String> remove(@PathVariable String[] backupLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        backupLogService.deleteBackupLogByIds(backupLogIds);
        return resultT;
    }
}

