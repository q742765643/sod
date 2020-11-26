package com.piesat.schedule.web.controller.syncdar;

import com.piesat.schedule.rpc.api.syncdar.SyncDarService;
import com.piesat.schedule.rpc.dto.syncdar.SyncDarDto;
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
 * @author cwh
 * @date 2020年 10月28日 17:12:20
 */
@RestController
@Api(value = "非结构化实时应用库同步", tags = {"非结构化实时应用库同步"})
@RequestMapping("/schedule/syncdar")
public class SyncDarController {
    @Autowired
    private SyncDarService syncDarService;

    @RequiresPermissions("schedule:syncdar:list")
    @ApiOperation(value = "分页查询非结构化实时应用库同步任务", notes = "分页查询非结构化实时应用库同步任务")
    @GetMapping("/list")
    public ResultT<PageBean<SyncDarDto>> list(SyncDarDto syncDar, int pageNum, int pageSize) {
        ResultT<PageBean<SyncDarDto>> resultT = new ResultT<>();
        PageForm<SyncDarDto> pageForm = new PageForm<>(pageNum, pageSize, syncDar);
        PageBean pageBean = syncDarService.selectSyncDarList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:syncdar:query")
    @ApiOperation(value = "根据ID查询非结构化实时应用库同步任务", notes = "根据ID查询非结构化实时应用库同步任务")
    @GetMapping(value = "/{syncDarId}")
    public ResultT<SyncDarDto> getInfo(@PathVariable String syncDarId) {
        ResultT<SyncDarDto> resultT = new ResultT<>();
        SyncDarDto syncDarDto = syncDarService.findSyncDarById(syncDarId);
        resultT.setData(syncDarDto);
        return resultT;
    }

    @RequiresPermissions("schedule:syncdar:add")
    @ApiOperation(value = "添加非结构化实时应用库同步任务", notes = "添加非结构化实时应用库同步任务")
    @Log(title = "非结构化实时应用库同步任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody SyncDarDto syncDar) {
        if (null == syncDar.getIsAlarm() || "".equals(syncDar.getIsAlarm())) {
            syncDar.setIsAlarm("1");
        }
        ResultT<String> resultT = new ResultT<>();
        syncDarService.saveSyncDar(syncDar);
        return resultT;
    }

    @RequiresPermissions("schedule:syncdar:edit")
    @ApiOperation(value = "修改非结构化实时应用库同步任务", notes = "修改非结构化实时应用库同步任务")
    @Log(title = "非结构化实时应用库同步任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody SyncDarDto syncDar) {
        if (null == syncDar.getIsAlarm() || "".equals(syncDar.getIsAlarm())) {
            syncDar.setIsAlarm("1");
        }
        ResultT<String> resultT = new ResultT<>();
        syncDarService.updateSyncDar(syncDar);
        return resultT;
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:syncdar:remove")
    @ApiOperation(value = "删除非结构化实时应用库同步任务", notes = "删除非结构化实时应用库同步任务")
    @Log(title = "非结构化实时应用库同步任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{syncDarIds}")
    public ResultT<String> remove(@PathVariable String[] syncDarIds) {
        ResultT<String> resultT = new ResultT<>();
        syncDarService.deleteSyncDarIds(syncDarIds);
        return resultT;
    }
}
