package com.piesat.schedule.web.controller.syncudtopc;

import com.piesat.schedule.rpc.api.syncudtopc.SyncUdToPcService;
import com.piesat.schedule.rpc.dto.syncudtopc.SyncUdToPcDto;
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
@Api(value = "非结构化到公有云同步", tags = {"非结构化到公有云同步"})
@RequestMapping("/schedule/syncudtopc")
public class SyncUdToPcController {
    @Autowired
    private SyncUdToPcService syncUdToPcService;

    @RequiresPermissions("schedule:syncudtopc:list")
    @ApiOperation(value = "分页查询非结构化到公有云同步任务", notes = "分页查询非结构化到公有云同步任务")
    @GetMapping("/list")
    public ResultT<PageBean<SyncUdToPcDto>> list(SyncUdToPcDto syncUdToPc, int pageNum, int pageSize) {
        ResultT<PageBean<SyncUdToPcDto>> resultT = new ResultT<>();
        PageForm<SyncUdToPcDto> pageForm = new PageForm<>(pageNum, pageSize, syncUdToPc);
        PageBean pageBean = syncUdToPcService.selectSyncUdToPcList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:syncudtopc:query")
    @ApiOperation(value = "根据ID查询非结构化到公有云同步任务", notes = "根据ID查询非结构化到公有云同步任务")
    @GetMapping(value = "/{syncUdToPcId}")
    public ResultT<SyncUdToPcDto> getInfo(@PathVariable String syncUdToPcId) {
        ResultT<SyncUdToPcDto> resultT = new ResultT<>();
        SyncUdToPcDto syncUdToPcDto = syncUdToPcService.findSyncUdToPcById(syncUdToPcId);
        resultT.setData(syncUdToPcDto);
        return resultT;
    }

    @RequiresPermissions("schedule:syncudtopc:add")
    @ApiOperation(value = "添加非结构化到公有云同步任务", notes = "添加非结构化到公有云同步任务")
    @Log(title = "交换系统同步任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody SyncUdToPcDto syncUdToPc) {
        if (null == syncUdToPc.getIsAlarm() || "".equals(syncUdToPc.getIsAlarm())) {
            syncUdToPc.setIsAlarm("1");
        }
        ResultT<String> resultT = new ResultT<>();
        syncUdToPcService.saveSyncUdToPc(syncUdToPc);
        return resultT;
    }

    @RequiresPermissions("schedule:syncudtopc:edit")
    @ApiOperation(value = "修改非结构化到公有云同步任务", notes = "修改非结构化到公有云同步任务")
    @Log(title = "结构化转文件同步任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody SyncUdToPcDto syncUdToPc) {
        if (null == syncUdToPc.getIsAlarm() || "".equals(syncUdToPc.getIsAlarm())) {
            syncUdToPc.setIsAlarm("1");
        }
        ResultT<String> resultT = new ResultT<>();
        syncUdToPcService.updateSyncUdToPc(syncUdToPc);
        return resultT;
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:syncudtopc:remove")
    @ApiOperation(value = "删除非结构化到公有云同步任务", notes = "删除非结构化到公有云同步任务")
    @Log(title = "非结构化到公有云同步管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{syncUdToPcIds}")
    public ResultT<String> remove(@PathVariable String[] syncUdToPcIds) {
        ResultT<String> resultT = new ResultT<>();
        syncUdToPcService.deleteSyncUdToPcIds(syncUdToPcIds);
        return resultT;
    }
}
