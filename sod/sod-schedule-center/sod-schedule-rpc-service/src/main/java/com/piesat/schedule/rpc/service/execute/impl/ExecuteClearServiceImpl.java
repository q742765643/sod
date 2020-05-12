package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.dao.clear.ClearDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
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
@Service("executeClearService")
public class ExecuteClearServiceImpl extends ExecuteBaseService implements ExecuteService {
    @Autowired
    private ClearDao clearDao;

    @Autowired
    private JobInfoLogService jobInfoLogService;


    @Override
    public JobInfoEntity getById(String id) {
        return clearDao.findById(id).get();
    }


    @Override
    public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {
         ClearEntity clearEntity= (ClearEntity) jobInfoEntity;
         if("XUGU".equals(clearEntity.getDatabaseType().toUpperCase())){
             jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.CLUSTER_SERIAL.name());
         }else{
             jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.TASK_SERIAL.name());
         }
    }
}

