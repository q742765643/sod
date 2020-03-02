package com.piesat.schedule.client.service.recover;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.recover.MetaRecoverLogDao;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 17:48
 **/
@Service
public class MetaRecoverLogService extends BaseService<MetaRecoverLogEntity> {
    @Autowired
    private MetaRecoverLogDao metaRecoverLogDao;
    @Override
    public BaseDao<MetaRecoverLogEntity> getBaseDao() {
        return metaRecoverLogDao;
    }
}

