package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.dao.backup.BackupDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.enums.ExecutorBlockStrategyEnum;
import com.piesat.schedule.rpc.mapstruct.BackupToLogMapstruct;
import com.piesat.schedule.rpc.service.JobInfoLogService;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.schedule.rpc.vo.Server;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 16:55
 **/
@Service("executeBackupService")
public class ExecuteBackupServiceImpl extends ExecuteBaseService implements ExecuteService {
    @Autowired
    private JobInfoLogService jobInfoLogService;
    @Autowired
    private BackupToLogMapstruct backupToLogMapstruct;
    @Autowired
    private BackupDao backupDao;


    @Override
    public String insertLog(JobInfoEntity jobInfo,Server server,String result,String logId){
        BackupEntity backupEntity= (BackupEntity) jobInfo;
        BackupLogEntity backupLogEntity=backupToLogMapstruct.toEntity(backupEntity);
        backupLogEntity.setJobId(backupEntity.getId());
        backupLogEntity.setId(logId);
        backupLogEntity.setExecutorAddress(server.getHost()+":"+server.getGrpcPort());
        backupLogEntity.setHandleCode(result);
        backupLogEntity.setTriggerTime(backupEntity.getTriggerLastTime());
        backupLogEntity.setHandleTime(new Date());
        backupLogEntity.setElapsedTime(0);
        backupLogEntity= (BackupLogEntity) jobInfoLogService.saveNotNull(backupLogEntity);
        return backupLogEntity.getId();
    }

    @Override
    public JobInfoEntity getById(String id) {
        return backupDao.findById(id).get();
    }


    @Override
    public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {
        jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.TASK_SERIAL.name());
    }
}

