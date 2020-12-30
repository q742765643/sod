package com.piesat.schedule.web.controller.synctofile;

import com.piesat.schedule.rpc.api.synctofile.SyncToFileLogService;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.schedule.rpc.dto.synctofile.SyncToFileLogDto;
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

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-29 20:12
 **/
@RestController
@Api(value = "数据同步日志接口", tags = {"数据同步日志接口"})
@RequestMapping("/schedule/synctofileLog")
public class SyncToFileLogController {
    @Autowired
    private SyncToFileLogService synctofileLogService;

    @RequiresPermissions("schedule:synctofileLog:list")
    @GetMapping("/list")
    public ResultT<PageBean> list(SyncToFileLogDto synctofileLog, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<SyncToFileLogDto> pageForm=new PageForm<>(pageNum,pageSize,synctofileLog);
        PageBean pageBean=synctofileLogService.selectSyncToFileLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:synctofileLog:query")
    @GetMapping(value = "/{synctofileLogId}")
    public ResultT<SyncToFileLogDto> getInfo(@PathVariable String synctofileLogId)
    {
        ResultT<SyncToFileLogDto> resultT=new ResultT<>();
        SyncToFileLogDto synctofileLogDto= synctofileLogService.findSyncToFileLogById(synctofileLogId);
        resultT.setData(synctofileLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:synctofileLog:remove")
    @Log(title = "备份任务日志管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{synctofileLogIds}")
    public  ResultT<String> remove(@PathVariable String[] synctofileLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        synctofileLogService.deleteSyncToFileLogByIds(synctofileLogIds);
        return resultT;
    }

    @ApiOperation(value = "数据同步日志导出", notes = "数据同步日志导出")
    @RequiresPermissions("schedule:synctofileLog:export")
    @GetMapping("/export")
    public void exportExcel(SyncToFileLogDto synctofileLogDto){
        synctofileLogService.exportExcel(synctofileLogDto);
    }
}

