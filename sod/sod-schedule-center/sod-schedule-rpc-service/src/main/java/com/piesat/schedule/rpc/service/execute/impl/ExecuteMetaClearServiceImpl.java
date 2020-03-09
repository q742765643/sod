package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.schedule.dao.backup.MetaBackupDao;
import com.piesat.schedule.dao.clear.MetaClearDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.enums.ExecutorBlockStrategyEnum;
import com.piesat.schedule.rpc.dto.clear.MetaClearDto;
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
@Service("executeMetaClearService")
public class ExecuteMetaClearServiceImpl extends ExecuteBaseService implements ExecuteService {
    @Autowired
    private MetaClearDao metaClearDao;
    @Override
    public JobInfoEntity getById(String id) {
        return metaClearDao.findById(id).get();
    }

    @Override
    public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {
        jobInfoEntity.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.TASK_SERIAL.name());
    }


}

