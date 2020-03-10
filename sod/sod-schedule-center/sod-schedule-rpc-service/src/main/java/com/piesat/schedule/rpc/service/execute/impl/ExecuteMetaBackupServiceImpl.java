package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.schedule.dao.backup.MetaBackupDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.enums.ExecutorBlockStrategyEnum;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.schedule.rpc.vo.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-28 14:32
 **/
@Service("executeMetaBackupService")
public class ExecuteMetaBackupServiceImpl  extends ExecuteBaseService implements ExecuteService {
    @Autowired
    private MetaBackupDao metaBackupDao;
    @Override
    public JobInfoEntity getById(String id) {
        return metaBackupDao.findById(id).get();
    }

    @Override
    public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {
        jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.TASK_SERIAL.name());
    }


}

