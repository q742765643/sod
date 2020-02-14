package com.piesat.dm.rpc.service;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.StorageConfigurationDao;
import com.piesat.dm.entity.StorageConfigurationEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.mapper.StorageConfigurationMapper;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.api.sync.SyncTaskService;
import com.piesat.schedule.rpc.dto.sync.SyncTaskDto;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:28
 */
@Service
public class StorageConfigurationGrpcService {
    @Autowired
    private StorageConfigurationDao storageConfigurationDao;
    @Autowired
    private StorageConfigurationMapper storageConfigurationMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataClassService dataClassService;
    @GrpcHthtClient
    private ClearService clearService;
    @GrpcHthtClient
    private SyncTaskService syncTaskService;
    @GrpcHthtClient
    private BackupService backupService;


    public ResultT updateColumnValue(String id, String column, String value) {
        //删除配置
        if("3".equals(value)){
            //获取策略配置
            StorageConfigurationEntity storage = this.storageConfigurationDao.findById(id).get();
            if (column.equals("storage_define_identifier")) {
                //删除存储结构
                dataClassService.deleteByDataClassId(storage.getDataClassId());
            }else if(column.equals("sync_identifier")){
                //删除同步配置
                String task_id = storage.getSyncId();
                if (StringUtils.isNotNullString(task_id)) {
                    SyncTaskDto syncTaskDto = syncTaskService.getDtoById(task_id);
                    if (syncTaskDto != null) {
                        syncTaskService.deleteSync(task_id);
                    }
                }
            }else if(column.equals("moveclean_identifier")){
                //删除迁移清楚
                String taskId = storage.getClearId();
                if (StringUtils.isNotNullString(taskId)) {
                    clearService.deleteClearByIds(new String[]{taskId});
                }
            }else if(column.equals("backup_identifier")){
                //删除备份
                String taskId = storage.getBackupId();
                if (StringUtils.isNotNullString(taskId)) {
                    backupService.deleteBackupByIds(new String[]{taskId});
                }
            }
        }
        //更新状态
        mybatisQueryMapper.updateStorageConfigurationStatus(id,column,value);

        return  ResultT.success();
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
}
