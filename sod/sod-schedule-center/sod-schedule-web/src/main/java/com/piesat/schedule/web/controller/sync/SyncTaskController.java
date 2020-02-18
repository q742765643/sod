package com.piesat.schedule.web.controller.sync;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.DataTableService;
import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.api.TableColumnService;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.TableColumnDto;
import com.piesat.schedule.rpc.api.sync.SyncTaskService;
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
    public ResultT<PageBean> list(SyncTaskDto syncTaskDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<SyncTaskDto> pageForm = new PageForm<>(pageNum, pageSize, syncTaskDto);
        PageBean pageBean = syncTaskService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
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
    public ResultT<PageBean> listLog(SyncTaskLogDto syncTaskLogDto, int pageNum, int pageSize) {
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
    public ResultT<SyncTaskDto> syncSaveUpdate(SyncTaskDto syncTaskDto, HttpServletRequest request) {

        //获取字段对应信息
        String[] targetTableIds = syncTaskDto.getTargetTableIds();
        List<Map<String, Object>> targets = new ArrayList<>();
        for (int i = 0; i < targetTableIds.length; i++) {
            String targetTableId = targetTableIds[i];
            String[] targetColumns = request.getParameterValues("targetColumn_" + targetTableId);// 第i组映射关系的目标字段
            String[] sourceColumns = request.getParameterValues("sourceColumn_" + targetTableId);// 第i组映射关系的源表字段
            // 拼接源表目标表映射关系
            StringBuffer mapping = new StringBuffer();
            for (int j = 0; j < targetColumns.length; j++) {
                String targetColumn = targetColumns[j];
                mapping.append("<" + targetColumn + ">" + sourceColumns[j] + "</" + targetColumn + ">");
                if (j < targetColumns.length - 1) {
                    mapping.append("\r\n");
                }
            }
            Map<String, Object> relation = new HashMap<>();
            relation.put("targetTable", targetTableId);
            relation.put("mapping", mapping.toString());
            targets.add(relation);
        }
        syncTaskDto.setTargetRelation(targets);

        //获取值表字段对应信息
        String targetVTableId = syncTaskDto.getTargetVTableId();
        String sourceVTableId = syncTaskDto.getSourceVTableId();
        if(StringUtils.isNotNullString(targetVTableId)){
            String[] targetColumns = request.getParameterValues("targetColumn_V_" + targetVTableId);// 值表目标表字段
            String[] sourceColumns = request.getParameterValues("sourceColumn_V_" + targetVTableId);// 值表源表字段
            //拼接源表目标表映射关系
            StringBuffer mapping = new StringBuffer();
            for (int j = 0; j < targetColumns.length; j++) {
                String targetColumn = targetColumns[j];
                mapping.append("<" + targetColumn + ">" + sourceColumns[j] + "</" + targetColumn + ">");
                if (j < targetColumns.length - 1) {
                    mapping.append("\r\n");
                }
            }

            //与键表之间的外键（两个表之间的外键必须保证字段名称一样）
            DataTableDto targetVTable = dataTableService.getDotById(targetVTableId);
            String linkKey = syncTaskDto.getLinkKey();
            String linkKeys = "";
            if(StringUtils.isNotNullString(linkKey)){
                linkKeys = "<" + targetVTable.getTableName() + ">";
                String[] fkeys = linkKey.split(",");
                for(int i = 0;i < fkeys.length;i++){
                    if(fkeys[i].contains("&")){
                        fkeys[i] = fkeys[i].split("&")[0];
                    }
                    linkKeys +=  "<" + fkeys[i] + ">" + fkeys[i] + "</" + fkeys[i] + ">";
                }
                linkKeys += "</" + targetVTable.getTableName() + ">";
            }
            Map<String, Object> relation = new HashMap<>();
            relation.put("sourceTable", sourceVTableId);
            relation.put("targetTable", targetVTableId);
            relation.put("mapping", mapping.toString());
            relation.put("linkKey", linkKeys);
            syncTaskDto.setSlaveRelation(relation);
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
    @GetMapping("/deleteSync/{taskId}")
    public ResultT<String> deleteSync(@PathVariable String taskId) {
        ResultT<String> resultT=new ResultT<>();
        this.syncTaskService.deleteSync(taskId);
        return  resultT;
    }

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

}
