package com.piesat.schedule.sync.client.Service.syncudtopc;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.synces.SyncEsLogDao;
import com.piesat.schedule.dao.syncudtopc.SyncUdToPcDao;
import com.piesat.schedule.dao.syncudtopc.SyncUdToPcLogDao;
import com.piesat.schedule.entity.synces.SyncEsLogEntity;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author cwh
 * @date 2020年 11月06日 16:19:49
 */
@Service
public class SyncUdToPcLogService extends BaseService<SyncUdToPcLogEntity> {
    @Autowired
    private SyncUdToPcLogDao syncUdToPcLogDao;

    @Override
    public BaseDao<SyncUdToPcLogEntity> getBaseDao() {
        return this.syncUdToPcLogDao;
    }

    public SyncUdToPcLogEntity getLatest(String jobId) {
        return this.syncUdToPcLogDao.getLatest(jobId);
    }
}
