package com.piesat.schedule.web.controller.sync;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.*;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.api.datatable.TableForeignKeyService;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.datatable.TableForeignKeyDto;
import com.piesat.schedule.rpc.api.sync.SyncTaskService;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.schedule.rpc.dto.sync.SyncTaskDto;
import com.piesat.schedule.rpc.dto.sync.SyncTaskLogDto;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description 数据同步
 * @date 2020/1/13 11:39
 */
@RestController
@RequestMapping("/schedule/sync")
@Api(value = "数据同步controller", tags = {"数据同步管理接口"})
public class SyncTaskController {
    @Autowired
    private SyncTaskService syncTaskService;
    @GrpcHthtClient
    private DatabaseService databaseService;
    @GrpcHthtClient
    private DataTableService dataTableService;
    @GrpcHthtClient
    private TableColumnService tableColumnService;
    @GrpcHthtClient
    private DictDataService dictDataService;
    @GrpcHthtClient
    private TableForeignKeyService tableForeignKeyService;

    /**
     * 获取分页数据接口
     *
     * @param syncTaskDto
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequiresPermissions("schedule:sync:list")
    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(SyncTaskDto syncTaskDto,
                                  @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<SyncTaskDto> pageForm = new PageForm<>(pageNum, pageSize, syncTaskDto);
        PageBean pageBean = syncTaskService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }


    /**
     * 启动
     *
     * @return
     */
    @RequiresPermissions("schedule:sync:restart")
    @GetMapping("/restart/{taskId}")
    @ApiOperation(value = "启动", notes = "启动")
    public ResultT restart(@PathVariable String taskId) {
     this.syncTaskService.restart(taskId);
        return ResultT.success();
    }

    /**
     * 批量启动
     *
     * @return
     */
    @RequiresPermissions("schedule:sync:batchRestart")
    @GetMapping("/batchRestart")
    @ApiOperation(value = "批量启动", notes = "批量启动")
    public ResultT batchRestart(String taskIds) {
        String[] split = taskIds.split(",");
        for (String id:split) {
            this.syncTaskService.restart(id);
        }
        return ResultT.success();
    }



    /**
     * 停止
     *
     * @return
     */
    @RequiresPermissions("schedule:sync:stop")
    @GetMapping("/stop/{taskId}")
    @ApiOperation(value = "停止", notes = "停止")
    public ResultT stop(@PathVariable String taskId) {
        this.syncTaskService.stop(taskId);
        return ResultT.success();
    }

    /**
     * 批量停止
     *
     * @return
     */
    @RequiresPermissions("schedule:sync:batchStop")
    @GetMapping("/batchStop")
    @ApiOperation(value = "批量停止", notes = "批量停止")
    public ResultT batchStop(String taskIds) {
        String[] split = taskIds.split(",");
        for (String id:split) {
            this.syncTaskService.stop(id);
        }
        return ResultT.success();
    }


    /**
     * 获取同步日志分页数据接口
     *
     * @param syncTaskLogDto
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequiresPermissions("schedule:sync:listLog")
    @GetMapping("/listLog")
    @ApiOperation(value = "分页查询日志接口", notes = "分页查询日志接口")
    public ResultT<PageBean> listLog(SyncTaskLogDto syncTaskLogDto,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<SyncTaskLogDto> pageForm = new PageForm<>(pageNum, pageSize, syncTaskLogDto);
        PageBean pageBean = syncTaskService.selectLogPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    /**
     * 获取全部物理库和专题库
     *
     * @return
     */
    @RequiresPermissions("schedule:sync:syncDatabaseDetail")
    @GetMapping("/syncDatabaseDetail")
    @ApiOperation(value = "获取全部物理库和专题库", notes = "获取全部物理库和专题库")
    public ResultT<List<Map<String, Object>>> syncDatabaseDetail() {
        ResultT<List<Map<String, Object>>> resultT = new ResultT<>();
        List<Map<String, Object>> databaseNames = databaseService.getDatabaseName();
        resultT.setData(databaseNames);
        return resultT;
    }

    /**
     * 根据物理库id查询所有表信息
     *
     * @param databaseId
     * @return
     */
    @ApiOperation(value = "根据物理库id查询所有表信息")
    @RequiresPermissions("schedule:sync:syncTableByDatabaseId")
    @GetMapping("/syncTableByDatabaseId")
    public ResultT syncTableByDatabaseId(String databaseId) {
        try {
            List<Map<String, Object>> r = dataTableService.getByDatabaseId(databaseId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    /**
     * 根据表id查询所有字段信息
     *
     * @param tableId
     * @return
     */
    @ApiOperation(value = "根据表id查询所有字段信息")
    @RequiresPermissions("schedule:sync:syncColumnByTableId")
    @GetMapping("/syncColumnByTableId")
    public ResultT syncColumnByTableId(String tableId) {
        try {
            List<TableColumnDto> r = tableColumnService.findByTableId(tableId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增同步任务")
    @RequiresPermissions("schedule:sync:syncSaveUpdate")
    @PostMapping("/syncSaveUpdate")
    public ResultT<SyncTaskDto> syncSaveUpdate(@RequestBody SyncTaskDto syncTaskDto, HttpServletRequest request) {

        //获取值表字段对应信息
        String targetVTableId = syncTaskDto.getTargetVTableId();
        String sourceVTableId = syncTaskDto.getSourceVTableId();
        if(StringUtils.isNotNullString(targetVTableId)){
            //获取目标表值表信息
            DataTableDto targetVTable = dataTableService.getDotById(targetVTableId);

            //键值表外键
            String linkKeys = "";
            if(targetVTable.getClassLogic() != null){
                List<TableForeignKeyDto> tableForeignKeyDtos = tableForeignKeyService.findByClassLogicId(targetVTable.getClassLogic().getId());
                if(tableForeignKeyDtos != null && tableForeignKeyDtos.size()>0){
                    linkKeys = "<" + targetVTable.getTableName() + ">";
                    for(int i = 0;i < tableForeignKeyDtos.size();i++){
                        String eleColumn = tableForeignKeyDtos.get(i).getEleColumn();
                        linkKeys +=  "<" + eleColumn + ">" + eleColumn + "</" + eleColumn + ">";
                    }
                    linkKeys += "</" + targetVTable.getTableName() + ">";
                }
            }
            syncTaskDto.getSlaveRelation().put("sourceVTableId", sourceVTableId);
            syncTaskDto.getSlaveRelation().put("targetVTableId", targetVTableId);
            syncTaskDto.getSlaveRelation().put("linkKey", linkKeys);
        }
        if(StringUtils.isNotNullString(syncTaskDto.getId())){
            syncTaskDto = syncTaskService.updateDto(syncTaskDto);
        }else{
            syncTaskDto = syncTaskService.saveDto(syncTaskDto);
        }

        ResultT<SyncTaskDto> resultT = new ResultT<>();
        resultT.setData(syncTaskDto);
        return resultT;
    }

    @ApiOperation(value = "删除同步任务")
    @RequiresPermissions("schedule:sync:deleteSync")
    @DeleteMapping("/deleteSync/{taskId}")
    public ResultT<String> deleteSync(@PathVariable String taskId) {
        ResultT<String> resultT=new ResultT<>();
        this.syncTaskService.deleteSync(taskId);
        return  resultT;
    }

    @RequiresPermissions("schedule:sync:getSyncById")
    @GetMapping(value = "/getSyncById/{taskId}")
    public ResultT<JSONObject> getSyncById(@PathVariable String taskId)
    {
        ResultT<JSONObject> resultT=new ResultT<>();
        JSONObject json=syncTaskService.getSyncJsonById(taskId);
        resultT.setData(json);
        return resultT;
    }

    @ApiOperation(value = "根据字典类型查询")
    @GetMapping("/getDictDataByType/{type}")
    public ResultT getDictDataByType(@PathVariable String type){
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType(type);
        return  ResultT.success(dictDataDtos);
    }

    @ApiOperation(value = "数据同步导出", notes = "数据同步导出")
    @RequiresPermissions("schedule:sync:export")
    @GetMapping("/export")
    public void exportExcel(SyncTaskDto syncTaskDto){
        syncTaskService.exportExcel(syncTaskDto);
    }

}
