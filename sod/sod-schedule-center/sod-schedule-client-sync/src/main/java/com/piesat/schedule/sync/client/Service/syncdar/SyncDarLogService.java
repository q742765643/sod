package com.piesat.schedule.sync.client.Service.syncdar;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.clear.MetaClearLogDao;
import com.piesat.schedule.dao.syncdar.SyncDarLogDao;
import com.piesat.schedule.entity.clear.MetaClearLogEntity;
import com.piesat.schedule.entity.syncdar.SyncDarLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cwh
 * @date 2020年 11月06日 16:19:49
 */
@Service
public class SyncDarLogService extends BaseService<SyncDarLogEntity> {
    @Autowired
    private SyncDarLogDao syncDarLogDao;
    @Override
    public BaseDao<SyncDarLogEntity> getBaseDao() {
        return this.syncDarLogDao;
    }
}
