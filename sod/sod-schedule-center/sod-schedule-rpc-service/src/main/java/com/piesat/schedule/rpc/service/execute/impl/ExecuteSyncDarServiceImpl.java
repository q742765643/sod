package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.schedule.dao.syncdar.SyncDarDao;
import com.piesat.schedule.dao.synces.SyncEsDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.enums.ExecutorBlockStrategyEnum;
import com.piesat.schedule.rpc.service.JobInfoLogService;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.schedule.rpc.vo.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 16:55
 **/
@Service("executeSyncDarService")
public class ExecuteSyncDarServiceImpl extends ExecuteBaseService implements ExecuteService {
    @Autowired
    private JobInfoLogService jobInfoLogService;

    @Autowired
    private SyncDarDao syncDarDao;


    @Override
    public JobInfoEntity getById(String id) {
        return syncDarDao.findById(id).get();
    }


    @Override
    public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {
        jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.TASK_SERIAL.name());
    }
}

