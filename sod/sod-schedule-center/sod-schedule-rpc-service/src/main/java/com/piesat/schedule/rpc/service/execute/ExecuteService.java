package com.piesat.schedule.rpc.service.execute;

import com.piesat.schedule.entity.JobInfoEntity;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 16:50
 **/
public interface ExecuteService {
    public void insertLog(JobInfoEntity jobInfo);

    public JobInfoEntity getById(String id);
}

