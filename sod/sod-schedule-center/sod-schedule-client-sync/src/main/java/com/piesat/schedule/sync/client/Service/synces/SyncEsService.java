package com.piesat.schedule.sync.client.Service.synces;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.synces.SyncEsDao;
import com.piesat.schedule.dao.synces.SyncEsLogDao;
import com.piesat.schedule.entity.synces.SyncEsEntity;
import com.piesat.schedule.entity.synces.SyncEsLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author cwh
 * @date 2020年 11月06日 16:19:49
 */
@Service
public class SyncEsService extends BaseService<SyncEsEntity> {
    @Autowired
    private SyncEsDao syncEsDao;
    @Override
    public BaseDao<SyncEsEntity> getBaseDao() {
        return this.syncEsDao;
    }

    public void updateLastTime(String id, Date lastTime){
        this.syncEsDao.updateLastTime(lastTime,id);
    }
}
