package com.piesat.schedule.sync.client.Service.syncdar;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.syncdar.SyncDarDao;
import com.piesat.schedule.entity.syncdar.SyncDarEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cwh
 * @date 2020年 11月06日 16:19:49
 */
@Service
public class SyncDarService extends BaseService<SyncDarEntity> {
    @Autowired
    private SyncDarDao syncDarDao;
    @Override
    public BaseDao<SyncDarEntity> getBaseDao() {
        return this.syncDarDao;
    }
}
