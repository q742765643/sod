package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.dao.move.MoveDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.schedule.enums.ExecutorBlockStrategyEnum;
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
    private JobInfoLogService jobInfoLogService;

    @Override
    public JobInfoEntity getById(String id) {
        return moveDao.findById(id).get();
    }


    @Override
    public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {
        jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.TASK_SERIAL.name());

    }
}

