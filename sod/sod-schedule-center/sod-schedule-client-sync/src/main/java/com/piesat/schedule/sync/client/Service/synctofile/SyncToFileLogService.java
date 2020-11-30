package com.piesat.schedule.sync.client.Service.synctofile;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.synces.SyncEsLogDao;
import com.piesat.schedule.dao.synctofile.SyncToFileLogDao;
import com.piesat.schedule.entity.synces.SyncEsLogEntity;
import com.piesat.schedule.entity.synctofile.SyncToFileLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cwh
 * @date 2020年 11月06日 16:19:49
 */
@Service
public class SyncToFileLogService extends BaseService<SyncToFileLogEntity> {
    @Autowired
    private SyncToFileLogDao syncToFileLogDao;
    @Override
    public BaseDao<SyncToFileLogEntity> getBaseDao() {
        return this.syncToFileLogDao;
    }
}
