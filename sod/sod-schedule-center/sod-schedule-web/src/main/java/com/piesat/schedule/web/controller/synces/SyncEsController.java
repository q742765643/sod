package com.piesat.schedule.web.controller.synces;

import com.piesat.schedule.rpc.api.synces.SyncEsService;
import com.piesat.schedule.rpc.dto.synces.SyncEsDto;
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
@Api(value="交换系统同步",tags = {"交换系统同步"})
@RequestMapping("/schedule/synces")
public class SyncEsController {
    @Autowired
    private SyncEsService syncEsService;

    @RequiresPermissions("schedule:synces:list")
    @ApiOperation(value = "分页查询交换系统同步任务", notes = "分页查询交换系统同步任务")
    @GetMapping("/list")
    public ResultT<PageBean<SyncEsDto>> list(SyncEsDto syncEs, int pageNum, int pageSize)
    {
        ResultT<PageBean<SyncEsDto>> resultT=new ResultT<>();
        PageForm<SyncEsDto> pageForm=new PageForm<>(pageNum,pageSize,syncEs);
        PageBean pageBean=syncEsService.selectSyncEsList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:synces:query")
    @ApiOperation(value = "根据ID查询交换系统同步任务", notes = "根据ID查询交换系统同步任务")
    @GetMapping(value = "/{syncEsId}")
    public ResultT<SyncEsDto> getInfo(@PathVariable String syncEsId)
    {
        ResultT<SyncEsDto> resultT=new ResultT<>();
        SyncEsDto syncEsDto= syncEsService.findSyncEsById(syncEsId);
        resultT.setData(syncEsDto);
        return resultT;
    }
    @RequiresPermissions("schedule:synces:add")
    @ApiOperation(value = "添加交换系统同步任务", notes = "添加交换系统同步任务")
    @Log(title = "交换系统同步任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody SyncEsDto syncEs)
    {
        if(null==syncEs.getIsAlarm()||"".equals(syncEs.getIsAlarm())){
            syncEs.setIsAlarm("1");
        }
        ResultT<String> resultT=new ResultT<>();
        syncEsService.saveSyncEs(syncEs);
        return resultT;
    }

    @RequiresPermissions("schedule:synces:edit")
    @ApiOperation(value = "修改交换系统同步任务", notes = "修改交换系统同步任务")
    @Log(title = "交换系统同步任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody SyncEsDto syncEs)
    {
        if(null==syncEs.getIsAlarm()||"".equals(syncEs.getIsAlarm())){
            syncEs.setIsAlarm("1");
        }
        ResultT<String> resultT=new ResultT<>();
        syncEsService.updateSyncEs(syncEs);
        return resultT;
    }
    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:synces:remove")
    @ApiOperation(value = "删除交换系统同步任务", notes = "删除交换系统同步任务")
    @Log(title = "交换系统同步任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{syncEsIds}")
    public  ResultT<String> remove(@PathVariable String[] syncEsIds)
    {
        ResultT<String> resultT=new ResultT<>();
        syncEsService.deleteSyncEsIds(syncEsIds);
        return resultT;
    }
}
