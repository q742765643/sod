package com.piesat.schedule.web.controller.synctofile;

import com.piesat.schedule.rpc.api.synctofile.SyncToFileService;
import com.piesat.schedule.rpc.dto.synctofile.SyncToFileDto;
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

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @date 2020年 10月28日 17:12:20
 */
@RestController
@Api(value = "结构化转文件同步", tags = {"结构化转文件同步"})
@RequestMapping("/schedule/synctofile")
public class SyncToFileController {
    @Autowired
    private SyncToFileService syncToFileService;

    @RequiresPermissions("schedule:synctofile:list")
    @ApiOperation(value = "分页查询结构化转文件同步任务", notes = "分页查询结构化转文件同步任务")
    @GetMapping("/list")
    public ResultT<PageBean<SyncToFileDto>> list(SyncToFileDto syncToFile, int pageNum, int pageSize) {
        ResultT<PageBean<SyncToFileDto>> resultT = new ResultT<>();
        PageForm<SyncToFileDto> pageForm = new PageForm<>(pageNum, pageSize, syncToFile);
        PageBean pageBean = syncToFileService.selectSyncToFileList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:synctofile:query")
    @ApiOperation(value = "根据ID查询结构化转文件同步任务", notes = "根据ID查询结构化转文件同步任务")
    @GetMapping(value = "/{syncToFileId}")
    public ResultT<SyncToFileDto> getInfo(@PathVariable String syncToFileId) {
        ResultT<SyncToFileDto> resultT = new ResultT<>();
        SyncToFileDto syncToFileDto = syncToFileService.findSyncToFileById(syncToFileId);
        resultT.setData(syncToFileDto);
        return resultT;
    }

    @RequiresPermissions("schedule:synctofile:add")
    @ApiOperation(value = "添加结构化转文件同步任务", notes = "添加结构化转文件同步任务")
    @Log(title = "交换系统同步任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody SyncToFileDto syncToFile) {
        if (null == syncToFile.getIsAlarm() || "".equals(syncToFile.getIsAlarm())) {
            syncToFile.setIsAlarm("1");
        }
        ResultT<String> resultT = new ResultT<>();
        syncToFileService.saveSyncToFile(syncToFile);
        return resultT;
    }

    @RequiresPermissions("schedule:synctofile:edit")
    @ApiOperation(value = "修改结构化转文件同步任务", notes = "修改结构化转文件同步任务")
    @Log(title = "结构化转文件同步任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody SyncToFileDto syncToFile) {
        if (null == syncToFile.getIsAlarm() || "".equals(syncToFile.getIsAlarm())) {
            syncToFile.setIsAlarm("1");
        }
        ResultT<String> resultT = new ResultT<>();
        syncToFileService.updateSyncToFile(syncToFile);
        return resultT;
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:synctofile:remove")
    @ApiOperation(value = "删除结构化转文件同步任务", notes = "删除结构化转文件同步任务")
    @Log(title = "结构化转文件同步任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{syncToFileIds}")
    public ResultT<String> remove(@PathVariable String[] syncToFileIds) {
        ResultT<String> resultT = new ResultT<>();
        syncToFileService.deleteSyncToFileIds(syncToFileIds);
        return resultT;
    }

    @ApiOperation(value = "数据配置导出", notes = "数据配置导出")
    @RequiresPermissions("schedule:synctofile:export")
    @GetMapping("/export")
    public void exportExcel(SyncToFileDto syncToFileDto){
        syncToFileService.exportExcel(syncToFileDto);
    }

    @ApiOperation(value = "查询同步任务物理库", notes = "查询同步任务物理库")
    @GetMapping("/findDatabase")
    public ResultT findDatabase(){
        ResultT resultT=new ResultT<>();
        List<Map<String,Object>> mapList=syncToFileService.findDatabase();
        resultT.setData(mapList);
        return resultT;
    }
    @ApiOperation(value = "查询需要同步资料列表", notes = "查询需要同步资料列表")
    @GetMapping("/findDataClassId")
    public ResultT findDataClassId(String databaseId,String dataClassId){
        ResultT resultT=new ResultT<>();
        List<Map<String,Object>> mapList=syncToFileService.findDataClassId(databaseId,dataClassId);
        resultT.setData(mapList);
        return resultT;
    }
}
