package com.piesat.schedule.web.controller.backup;

import com.piesat.schedule.rpc.api.backup.BackupLogService;
import com.piesat.schedule.rpc.api.backup.MetaBackupLogService;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
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

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 11:01
 **/
@RestController
@Api(value="元数据数据备份日志接口",tags = {"元数据数据备份日志接口"})
@RequestMapping("/schedule/metaBackupLog")
public class MetaBackupLogController {

    @Autowired
    private MetaBackupLogService metaBackupLogService;

    @RequiresPermissions("schedule:metaBackupLog:list")
    @ApiOperation(value = "分页查询元数据备份任务日志", notes = "分页查询元数据备份任务日志")
    @GetMapping("/list")
    public ResultT<PageBean<MetaBackupLogDto>> list(MetaBackupLogDto metaBackupLogDto, int pageNum, int pageSize)
    {
        ResultT<PageBean<MetaBackupLogDto>> resultT=new ResultT<>();
        PageForm<MetaBackupLogDto> pageForm=new PageForm<>(pageNum,pageSize,metaBackupLogDto);
        PageBean pageBean=metaBackupLogService.selectMetaBackupLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:metaBackupLog:query")
    @ApiOperation(value = "根据ID查询元数据备份任务日志", notes = "根据ID查询元数据备份任务日志")
    @GetMapping(value = "/{metaBackupLogId}")
    public ResultT<MetaBackupLogDto> getInfo(@PathVariable String metaBackupLogId)
    {
        ResultT<MetaBackupLogDto> resultT=new ResultT<>();
        MetaBackupLogDto metaBackupLogDto= metaBackupLogService.findMetaBackupLogById(metaBackupLogId);
        resultT.setData(metaBackupLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:metaBackupLog:remove")
    @Log(title = "元数据备份任务日志管理", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除元数据备份任务日志", notes = "删除元数据备份任务日志")
    @DeleteMapping("/{metaBackupLogIds}")
    public  ResultT<String> remove(@PathVariable String[] metaBackupLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        metaBackupLogService.deleteMetaBackupLogByIds(metaBackupLogIds);
        return resultT;
    }
    @ApiOperation(value = "元数据备份日志导出", notes = "元数据备份日志导出")
    @RequiresPermissions("schedule:metaBackupLog:export")
    @GetMapping("/export")
    public void exportExcel(MetaBackupLogDto metaBackupLogDto){
        metaBackupLogService.exportExcel(metaBackupLogDto);
    }

}

