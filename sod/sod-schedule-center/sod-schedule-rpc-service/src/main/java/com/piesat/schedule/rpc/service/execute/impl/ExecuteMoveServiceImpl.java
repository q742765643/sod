package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.dao.move.MoveDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.schedule.rpc.mapstruct.MoveToLogMapstruct;
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
@Service("executeMoveService")
public class ExecuteMoveServiceImpl extends ExecuteBaseService implements ExecuteService{
    @Autowired
    private MoveDao moveDao;
    @Autowired
    private MoveToLogMapstruct moveToLogMapstruct;
    @Autowired
    private JobInfoLogService jobInfoLogService;
    @Override
    public void insertLog(JobInfoEntity jobInfo) {
       /* MoveEntity moveEntity= (MoveEntity) jobInfo;
        MoveLogEntity moveLogEntity=moveToLogMapstruct.toEntity(moveEntity);
        moveLogEntity.setJobId(jobInfo.getId());
        moveLogEntity.setId(null);
        moveLogEntity.setExecutorAddress("192.168.0.12:6000");
        moveLogEntity.setHandleCode("0");
        moveLogEntity.setTriggerTime(moveEntity.getTriggerLastTime());
        moveLogEntity.setHandleTime(new Date());
        moveLogEntity.setElapsedTime(10000);
        jobInfoLogService.saveNotNull(moveLogEntity);*/
    }

    @Override
    public JobInfoEntity getById(String id) {
        return moveDao.findById(id).get();
    }

    @Override
    public Server operationalControl(JobInfoEntity jobInfoEntity, List<Server> servers, ResultT<String> resultT) {
        return servers.get(0);
    }
}

