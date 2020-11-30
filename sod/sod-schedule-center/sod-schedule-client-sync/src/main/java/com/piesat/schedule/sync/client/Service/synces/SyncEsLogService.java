package com.piesat.schedule.sync.client.Service.synces;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.synces.SyncEsLogDao;
import com.piesat.schedule.entity.synces.SyncEsLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cwh
 * @date 2020年 11月06日 16:19:49
 */
@Service
public class SyncEsLogService extends BaseService<SyncEsLogEntity> {
    @Autowired
    private SyncEsLogDao syncEsLogDao;
    @Override
    public BaseDao<SyncEsLogEntity> getBaseDao() {
        return this.syncEsLogDao;
    }
}
