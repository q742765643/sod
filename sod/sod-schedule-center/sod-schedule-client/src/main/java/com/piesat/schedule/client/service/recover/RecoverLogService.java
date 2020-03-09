package com.piesat.schedule.client.service.recover;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.recover.MetaRecoverLogDao;
import com.piesat.schedule.dao.recover.RecoverLogDao;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 17:48
 **/
@Service
public class RecoverLogService extends BaseService<RecoverLogEntity> {
    @Autowired
    private RecoverLogDao recoverLogDao;
    @Override
    public BaseDao<RecoverLogEntity> getBaseDao() {
        return recoverLogDao;
    }
}

