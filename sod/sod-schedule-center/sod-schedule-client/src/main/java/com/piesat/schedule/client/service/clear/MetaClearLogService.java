package com.piesat.schedule.client.service.clear;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.clear.MetaClearLogDao;
import com.piesat.schedule.entity.clear.MetaClearLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 16:57
 **/
@Service
public class MetaClearLogService extends BaseService<MetaClearLogEntity>{
    @Autowired
    private MetaClearLogDao metaClearLogDao;
    @Override
    public BaseDao<MetaClearLogEntity> getBaseDao() {
        return metaClearLogDao;
    }
}

