package com.piesat.schedule.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.JobInfoLogDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.JobInfoLogEntity;
import com.piesat.schedule.mapper.JobInfoLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 15:08
 **/
@Service
public class JobInfoLogService extends BaseService<JobInfoLogEntity>{
    @Autowired
    private JobInfoLogDao jobInfoLogDao;
    @Autowired
    private JobInfoLogMapper jobInfoLogMapper;
    @Override
    public BaseDao<JobInfoLogEntity> getBaseDao() {
        return jobInfoLogDao;
    }

    public JobInfoLogEntity selectMaxTriggerTimeByJobId(String jobId){
       return jobInfoLogMapper.selectMaxTriggerTimeByJobId(jobId);
    }

    public JobInfoLogEntity saveJobInfoLog(JobInfoLogEntity jobInfoLog){
        return this.saveNotNull(jobInfoLog);
    }
}

