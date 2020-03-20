package com.piesat.schedule.web.controller.clear;

import com.piesat.schedule.rpc.api.clear.ClearLogService;
import com.piesat.schedule.rpc.dto.clear.ClearDto;
import com.piesat.schedule.rpc.dto.clear.ClearLogDto;
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
 * @create: 2019-12-29 20:15
 **/
@RestController
@Api(value = "数据清除日志接口", tags = {"数据清除日志接口"})
@RequestMapping("/schedule/clearLog")
public class ClearLogController {
    @Autowired
    private ClearLogService clearLogService;

    @RequiresPermissions("schedule:clearLog:list")
    @GetMapping("/list")
    public ResultT<PageBean> list(ClearLogDto clearLog, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<ClearLogDto> pageForm=new PageForm<>(pageNum,pageSize,clearLog);
        PageBean pageBean=clearLogService.selectClearLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:clearLog:query")
    @GetMapping(value = "/{clearLogId}")
    public ResultT<ClearLogDto> getInfo(@PathVariable String clearLogId)
    {
        ResultT<ClearLogDto> resultT=new ResultT<>();
        ClearLogDto clearLogDto= clearLogService.findClearLogById(clearLogId);
        resultT.setData(clearLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:clearLog:remove")
    @Log(title = "清除任务日志管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{clearLogIds}")
    public  ResultT<String> remove(@PathVariable String[] clearLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        clearLogService.deleteClearLogByIds(clearLogIds);
        return resultT;
    }
    @ApiOperation(value = "数据清除日志导出", notes = "数据清除日志导出")
    @RequiresPermissions("schedule:clearLog:export")
    @GetMapping("/export")
    public void exportExcel(ClearLogDto clearLogDto){
        clearLogService.exportExcel(clearLogDto);
    }
}

