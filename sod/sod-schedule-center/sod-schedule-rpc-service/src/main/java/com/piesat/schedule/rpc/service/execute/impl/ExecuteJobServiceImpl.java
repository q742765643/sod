package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.schedule.dao.JobInfoDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.schedule.rpc.vo.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/5/7 16:44
 */
@Service("executeJobService")
public class ExecuteJobServiceImpl extends ExecuteBaseService implements ExecuteService {
    @Autowired
    private JobInfoDao jobInfoDao;

    @Override
    public JobInfoEntity getById(String id) {
        return jobInfoDao.findById(id).get();
    }

    @Override
    public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {

    }
}
