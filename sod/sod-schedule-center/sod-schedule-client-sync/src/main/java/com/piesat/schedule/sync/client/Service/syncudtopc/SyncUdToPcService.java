package com.piesat.schedule.sync.client.Service.syncudtopc;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.syncudtopc.SyncUdToPcDao;
import com.piesat.schedule.dao.syncudtopc.SyncUdToPcLogDao;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcEntity;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author cwh
 * @date 2020年 11月06日 16:19:49
 */
@Service
public class SyncUdToPcService extends BaseService<SyncUdToPcEntity> {
    @Autowired
    private SyncUdToPcDao syncUdToPcDao;

    @Override
    public BaseDao<SyncUdToPcEntity> getBaseDao() {
        return this.syncUdToPcDao;
    }

    public void updateLastTime(Date lastTime, String id) {
        this.syncUdToPcDao.updateLastTime(lastTime, id);
    }
}
