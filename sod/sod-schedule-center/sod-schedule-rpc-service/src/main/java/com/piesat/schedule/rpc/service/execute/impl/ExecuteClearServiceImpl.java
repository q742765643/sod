package com.piesat.schedule.rpc.service.execute.impl;

import com.piesat.schedule.dao.clear.ClearDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.rpc.mapstruct.ClearToLogMapstruct;
import com.piesat.schedule.rpc.service.JobInfoLogService;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 17:45
 **/
@Service("executeClearService")
public class ExecuteClearServiceImpl implements ExecuteService {
    @Autowired
    private ClearDao clearDao;
    @Autowired
    private ClearToLogMapstruct clearToLogMapstruct;
    @Autowired
    private JobInfoLogService jobInfoLogService;
    @Override
    public void insertLog(JobInfoEntity jobInfo) {
        ClearEntity clearEntity= (ClearEntity) jobInfo;
        ClearLogEntity clearLogEntity=clearToLogMapstruct.toEntity(clearEntity);
        clearLogEntity.setJobId(clearEntity.getId());
        clearLogEntity.setId(null);
        clearLogEntity.setExecutorAddress("192.168.0.12:6000");
        clearLogEntity.setHandleCode("0");
        clearLogEntity.setTriggerTime(clearEntity.getTriggerLastTime());
        clearLogEntity.setHandleTime(new Date());
        clearLogEntity.setElapsedTime(10000);
        jobInfoLogService.saveNotNull(clearLogEntity);
    }

    @Override
    public JobInfoEntity getById(String id) {
        return clearDao.findById(id).get();
    }
}

