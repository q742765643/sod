package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.schedule.dao.move.MoveDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.schedule.rpc.mapstruct.MoveToLogMapstruct;
import com.piesat.schedule.rpc.service.JobInfoLogService;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 17:45
 **/
@Service("executeMoveService")
public class ExecuteMoveServiceImpl implements ExecuteService{
    @Autowired
    private MoveDao moveDao;
    @Autowired
    private MoveToLogMapstruct moveToLogMapstruct;
    @Autowired
    private JobInfoLogService jobInfoLogService;
    @Override
    public void insertLog(JobInfoEntity jobInfo) {
        MoveEntity moveEntity= (MoveEntity) jobInfo;
        MoveLogEntity moveLogEntity=moveToLogMapstruct.toEntity(moveEntity);
        moveLogEntity.setJobId(jobInfo.getId());
        moveLogEntity.setId(null);
        jobInfoLogService.saveNotNull(moveLogEntity);
    }

    @Override
    public JobInfoEntity getById(String id) {
        return moveDao.findById(id).get();
    }
}

