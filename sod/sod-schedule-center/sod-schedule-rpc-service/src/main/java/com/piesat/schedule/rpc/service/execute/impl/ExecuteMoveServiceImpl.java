package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.dao.move.MoveDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.schedule.enums.ExecutorBlockStrategyEnum;
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
    public String insertLog(JobInfoEntity jobInfo,Server server,String result,String logId) {
        MoveEntity moveEntity= (MoveEntity) jobInfo;
        MoveLogEntity moveLogEntity=moveToLogMapstruct.toEntity(moveEntity);
        moveLogEntity.setId(logId);
        moveLogEntity.setJobId(jobInfo.getId());
        moveLogEntity.setExecutorAddress(server.getHost()+":"+server.getGrpcPort());
        moveLogEntity.setHandleCode(result);
        moveLogEntity.setTriggerTime(moveEntity.getTriggerLastTime());
        moveLogEntity.setHandleTime(new Date());
        moveLogEntity.setElapsedTime(0);
        moveLogEntity= (MoveLogEntity) jobInfoLogService.saveNotNull(moveLogEntity);
        return moveLogEntity.getId();
    }

    @Override
    public JobInfoEntity getById(String id) {
        return moveDao.findById(id).get();
    }


    @Override
    public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {
        jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.TASK_SERIAL.name());

    }
}

