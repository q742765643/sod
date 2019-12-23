package com.piesat.schedule.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.JobInfoDao;
import com.piesat.schedule.entity.JobInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 16:29
 **/
@Service
public class JobInfoServiceImpl extends BaseService<JobInfoEntity> {
    @Autowired
    private JobInfoDao jobInfoDao;
    @Override
    public BaseDao<JobInfoEntity> getBaseDao() {
        return jobInfoDao;
    }
}

