package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.dao.clear.ClearDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
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
    public void insertLog(JobInfoEntity jobInfo) {
        /*ClearEntity clearEntity= (ClearEntity) jobInfo;
        ClearLogEntity clearLogEntity=clearToLogMapstruct.toEntity(clearEntity);
        clearLogEntity.setJobId(clearEntity.getId());
        clearLogEntity.setId(null);
        clearLogEntity.setExecutorAddress("192.168.0.12:6000");
        clearLogEntity.setHandleCode("0");
        clearLogEntity.setTriggerTime(clearEntity.getTriggerLastTime());
        clearLogEntity.setHandleTime(new Date());
        clearLogEntity.setElapsedTime(10000);
        jobInfoLogService.saveNotNull(clearLogEntity);*/
    }

    @Override
    public JobInfoEntity getById(String id) {
        return clearDao.findById(id).get();
    }

    @Override
    public Server operationalControl(JobInfoEntity jobInfoEntity, List<Server> servers, ResultT<String> resultT) {
        return servers.get(0);
    }
}

