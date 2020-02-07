package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.dao.clear.ClearDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.enums.ExecutorBlockStrategyEnum;
import com.piesat.schedule.rpc.mapstruct.ClearToLogMapstruct;
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
 * @create: 2019-12-27 17:45
 **/
@Service("executeClearService")
public class ExecuteClearServiceImpl extends ExecuteBaseService implements ExecuteService {
    @Autowired
    private ClearDao clearDao;
    @Autowired
    private ClearToLogMapstruct clearToLogMapstruct;
    @Autowired
    private JobInfoLogService jobInfoLogService;
    @Override
    public String insertLog(JobInfoEntity jobInfo,Server server,String result,String logId) {
        ClearEntity clearEntity= (ClearEntity) jobInfo;
        ClearLogEntity clearLogEntity=clearToLogMapstruct.toEntity(clearEntity);
        clearLogEntity.setId(logId);
        clearLogEntity.setJobId(clearEntity.getId());
        clearLogEntity.setExecutorAddress(server.getHost()+":"+server.getGrpcPort());
        clearLogEntity.setHandleCode(result);
        clearLogEntity.setTriggerTime(clearEntity.getTriggerLastTime());
        clearLogEntity.setHandleTime(new Date());
        clearLogEntity.setElapsedTime(0);
        clearLogEntity= (ClearLogEntity) jobInfoLogService.saveNotNull(clearLogEntity);
        return clearLogEntity.getId();
    }

    @Override
    public JobInfoEntity getById(String id) {
        return clearDao.findById(id).get();
    }


    @Override
    public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {
         ClearEntity clearEntity= (ClearEntity) jobInfoEntity;
         if("XUGU".equals(clearEntity.getDatabaseType())){
             jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.CLUSTER_SERIAL.name());
         }else{
             jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.TASK_SERIAL.name());
         }
    }
}

