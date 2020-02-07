package com.piesat.schedule.rpc.service.execute;

import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.util.ResultT;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 16:50
 **/
public interface ExecuteService {
    public void executeBusiness(JobInfoEntity jobInfoEntity,ResultT<String> resultT);

    //public void insertLog(JobInfoEntity jobInfo);

    public JobInfoEntity getById(String id);
}

