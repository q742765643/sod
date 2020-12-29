package com.piesat.schedule.web.controller.syncudtopc;


import com.piesat.schedule.rpc.api.syncudtopc.SyncUdToPcLogService;
import com.piesat.schedule.rpc.dto.syncudtopc.SyncUdToPcDto;
import com.piesat.schedule.rpc.dto.syncudtopc.SyncUdToPcLogDto;
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
@RequestMapping("/schedule/syncudtopcLog")
public class SyncUdToPcLogController {
    @Autowired
    private SyncUdToPcLogService syncUdToPcLogService;

    @RequiresPermissions("schedule:syncudtopcLog:list")
    @GetMapping("/list")
    public ResultT<PageBean> list(SyncUdToPcLogDto syncUdToPcLogDto, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<SyncUdToPcLogDto> pageForm=new PageForm<>(pageNum,pageSize,syncUdToPcLogDto);
        PageBean pageBean=syncUdToPcLogService.selectSyncUdToPcLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:syncudtopcLog:query")
    @GetMapping(value = "/{syncudtopcLogId}")
    public ResultT<SyncUdToPcLogDto> getInfo(@PathVariable String syncudtopcLogId)
    {
        ResultT<SyncUdToPcLogDto> resultT=new ResultT<>();
        SyncUdToPcLogDto syncUdToPcLogDto= syncUdToPcLogService.findSyncUdToPcLogById(syncudtopcLogId);
        resultT.setData(syncUdToPcLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:syncudtopcLog:remove")
    @Log(title = "备份任务日志管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{syncudtopcLogIds}")
    public  ResultT<String> remove(@PathVariable String[] syncUdToPcLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        syncUdToPcLogService.deleteSyncUdToPcLogByIds(syncUdToPcLogIds);
        return resultT;
    }

    @ApiOperation(value = "数据同步日志导出", notes = "数据同步日志导出")
    @RequiresPermissions("schedule:synctofileLog:export")
    @GetMapping("/export")
    public void exportExcel(SyncUdToPcLogDto syncUdToPcLogDto){
        syncUdToPcLogService.exportExcel(syncUdToPcLogDto);
    }
}

