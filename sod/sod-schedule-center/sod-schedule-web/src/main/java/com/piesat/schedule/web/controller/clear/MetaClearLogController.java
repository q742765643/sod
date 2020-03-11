package com.piesat.schedule.web.controller.clear;

import com.piesat.schedule.rpc.api.backup.MetaBackupLogService;
import com.piesat.schedule.rpc.api.clear.MetaClearLogService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.MetaBackupLogDto;
import com.piesat.schedule.rpc.dto.clear.MetaClearLogDto;
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
 * @create: 2020-03-09 17:39
 **/
@RestController
@Api(value="元数据数据清除日志接口",tags = {"元数据数据清除日志接口"})
@RequestMapping("/schedule/metaClearLog")
public class MetaClearLogController {
    @Autowired
    private MetaClearLogService metaClearLogService;


    @RequiresPermissions("schedule:metaClearLog:list")
    @ApiOperation(value = "分页查询元数据清除任务日志", notes = "分页查询元数据清除任务日志")
    @GetMapping("/list")
    public ResultT<PageBean<MetaClearLogDto>> list(MetaClearLogDto metaClearLogDto, int pageNum, int pageSize)
    {
        ResultT<PageBean<MetaClearLogDto>> resultT=new ResultT<>();
        PageForm<MetaClearLogDto> pageForm=new PageForm<>(pageNum,pageSize,metaClearLogDto);
        PageBean pageBean=metaClearLogService.selectMetaClearLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:metaClearLog:query")
    @ApiOperation(value = "根据ID查询元数据清除任务日志", notes = "根据ID查询元数据清除任务日志")
    @GetMapping(value = "/{metaClearLogId}")
    public ResultT<MetaClearLogDto> getInfo(@PathVariable String metaClearLogId)
    {
        ResultT<MetaClearLogDto> resultT=new ResultT<>();
        MetaClearLogDto metaClearLogDto= metaClearLogService.findMetaClearLogById(metaClearLogId);
        resultT.setData(metaClearLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:metaClearLog:remove")
    @Log(title = "元数据清除任务日志管理", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除元数据清除任务日志", notes = "删除元数据清除任务日志")
    @DeleteMapping("/{metaClearLogIds}")
    public  ResultT<String> remove(@PathVariable String[] metaClearLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        metaClearLogService.deleteMetaClearLogByIds(metaClearLogIds);
        return resultT;
    }

    @ApiOperation(value = "元数据清除日志导出", notes = "元数据清除日志导出")
    @RequiresPermissions("schedule:metaClearLog:export")
    @GetMapping("/export")
    public void exportExcel(MetaClearLogDto metaClearLogDto){
        metaClearLogService.exportExcel(metaClearLogDto);
    }
}

