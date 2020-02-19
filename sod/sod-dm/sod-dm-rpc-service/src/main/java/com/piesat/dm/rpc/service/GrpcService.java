package com.piesat.dm.rpc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.DatabaseDao;
import com.piesat.dm.dao.DatabaseDefineDao;
import com.piesat.dm.dao.StorageConfigurationDao;
import com.piesat.dm.entity.DatabaseDefineEntity;
import com.piesat.dm.entity.StorageConfigurationEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.api.LogicDefineService;
import com.piesat.dm.rpc.dto.LogicDatabaseDto;
import com.piesat.dm.rpc.dto.LogicDefineDto;
import com.piesat.dm.rpc.dto.LogicStorageTypesDto;
import com.piesat.dm.rpc.mapper.StorageConfigurationMapper;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.api.sync.SyncTaskService;
import com.piesat.schedule.rpc.dto.sync.SyncTaskDto;
import com.piesat.sod.system.rpc.api.ManageFieldService;
import com.piesat.sod.system.rpc.dto.ManageFieldDto;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:28
 */
@Service
public class GrpcService {
    @Autowired
    private StorageConfigurationDao storageConfigurationDao;
    @Autowired
    private StorageConfigurationMapper storageConfigurationMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private LogicDefineService logicDefineService;
    @Autowired
    private DatabaseDefineDao databaseDefineDao;
    @Autowired
    private DataLogicService dataLogicService;

    @GrpcHthtClient
    private DictDataService dictDataService;
    @GrpcHthtClient
    private ClearService clearService;
    @GrpcHthtClient
    private SyncTaskService syncTaskService;
    @GrpcHthtClient
    private BackupService backupService;


    public ResultT updateColumnValue(String id, String column, String value) {
        //删除配置
        if ("3".equals(value)) {
            //获取策略配置
            StorageConfigurationEntity storage = this.storageConfigurationDao.findById(id).get();
            if (column.equals("storage_define_identifier")) {
                //删除存储结构
                dataClassService.deleteByDataClassId(storage.getDataClassId());
            } else if (column.equals("sync_identifier")) {
                //删除同步配置
                String task_id = storage.getSyncId();
                if (StringUtils.isNotNullString(task_id)) {
                    SyncTaskDto syncTaskDto = syncTaskService.getDtoById(task_id);
                    if (syncTaskDto != null) {
                        syncTaskService.deleteSync(task_id);
                    }
                }
            } else if (column.equals("moveclean_identifier")) {
                //删除迁移清楚
                String taskId = storage.getClearId();
                if (StringUtils.isNotNullString(taskId)) {
                    clearService.deleteClearByIds(new String[]{taskId});
                }
            } else if (column.equals("backup_identifier")) {
                //删除备份
                String taskId = storage.getBackupId();
                if (StringUtils.isNotNullString(taskId)) {
                    backupService.deleteBackupByIds(new String[]{taskId});
                }
            }
        }
        //更新状态
        mybatisQueryMapper.updateStorageConfigurationStatus(id, column, value);

        return ResultT.success();
    }

    public ResultT deleteById(String id) {
        StorageConfigurationEntity storage = this.storageConfigurationDao.findById(id).get();
        //删除存储结构
        dataClassService.deleteByDataClassId(storage.getDataClassId());
        //删除同步配置
        if (StringUtils.isNotNullString(storage.getSyncId())) {
            SyncTaskDto syncTaskDto = syncTaskService.getDtoById(storage.getSyncId());
            if (syncTaskDto != null) {
                syncTaskService.deleteSync(storage.getSyncId());
            }
        }
        //删除迁移清楚
        if (StringUtils.isNotNullString(storage.getClearId())) {
            clearService.deleteClearByIds(new String[]{storage.getClearId()});
        }
        //删除备份
        if (StringUtils.isNotNullString(storage.getBackupId())) {
            backupService.deleteBackupByIds(new String[]{storage.getBackupId()});
        }
        //删除配置信息
        storageConfigurationDao.deleteById(id);
        return ResultT.success();
    }

    public List<Map<String, Object>> getLogicInfo() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        // 获取逻辑库类型
        List<LogicDefineDto> logiclist = logicDefineService.all();
        //查询物理库-逻辑库
        List<Map<String, Object>> distinctDatabaseAndLogic = dataLogicService.getDistinctDatabaseAndLogic();
        //查询所有的表类型
        Map<String, String> dictMap = new HashMap<String, String>();
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType("table_type");
        if (dictDataDtos != null) {
            for (DictDataDto dictDataDto : dictDataDtos) {
                dictMap.put(dictDataDto.getDictValue(), dictDataDto.getDictLabel());//E_table 要素表
            }
        }

        if (logiclist != null && logiclist.size() > 0) {
            for (int i = logiclist.size() - 1; i >= 0; i--) {
                LogicDefineDto logicDefineDto = logiclist.get(i);
                Map<String, Object> logicDefineMap = JSONObject.parseObject(JSON.toJSONString(logicDefineDto));
                //逻辑库对应的表类型
                List<LogicStorageTypesDto> logicStorageTypesEntityList = logicDefineDto.getLogicStorageTypesEntityList();
                List<Map<String, Object>> tableTypeList = new ArrayList<Map<String, Object>>();
                if (logicStorageTypesEntityList != null) {
                    for (LogicStorageTypesDto logicStorageTypesDto : logicStorageTypesEntityList) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        String storageType = logicStorageTypesDto.getStorageType();
                        map.put("key", storageType);
                        map.put("name", dictMap.get(storageType));
                        tableTypeList.add(map);
                    }
                }
                logicDefineMap.put("tableType", tableTypeList);

                //逻辑库对应的物理库
                List<Map<String, Object>> databaseList = new ArrayList<Map<String, Object>>();
                for (Map<String, Object> databaseLogic : distinctDatabaseAndLogic) {
                    if (logicDefineDto.getLogicFlag().toString().equals(String.valueOf(databaseLogic.get("logic_flag")))) {
                        databaseList.add(databaseLogic);
                    }
                }
                if (databaseList.size() != 0) {
                    logicDefineMap.put("physics", databaseList);
                    result.add(logicDefineMap);
                }
            }
        }

        return result;
    }

    public List<LogicDefineDto> getAllLogicDefine() {
        List<LogicDefineDto> all = this.logicDefineService.all();
        for (LogicDefineDto ld : all) {
            List<LogicDatabaseDto> logicDatabaseEntityList = ld.getLogicDatabaseEntityList();
            for (LogicDatabaseDto ldb : logicDatabaseEntityList) {
                ldb.setDatabaseName(this.databaseDefineDao.findById(ldb.getDatabaseDefineId()).get().getDatabaseName());
            }
            List<LogicStorageTypesDto> logicStorageTypesEntityList = ld.getLogicStorageTypesEntityList();
            for (LogicStorageTypesDto ls:logicStorageTypesEntityList) {
                ls.setName(this.dictDataService.findByDictTypeAndDictValue("sys_storage_type",ls.getStorageType()).getDictLabel());
            }
        }
        return all;
    }

}
