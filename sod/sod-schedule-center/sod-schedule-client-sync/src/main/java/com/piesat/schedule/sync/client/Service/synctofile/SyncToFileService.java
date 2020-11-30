package com.piesat.schedule.sync.client.Service.synctofile;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.synctofile.SyncToFileDao;
import com.piesat.schedule.dao.synctofile.SyncToFileLogDao;
import com.piesat.schedule.entity.synctofile.SyncToFileEntity;
import com.piesat.schedule.entity.synctofile.SyncToFileLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author cwh
 * @date 2020年 11月06日 16:19:49
 */
@Service
public class SyncToFileService extends BaseService<SyncToFileEntity> {
    @Autowired
    private SyncToFileDao syncToFileDao;

    @Override
    public BaseDao<SyncToFileEntity> getBaseDao() {
        return this.syncToFileDao;
    }

    public void updateLastTime(Date lastTime, String id) {
        this.syncToFileDao.updateLastTime(lastTime, id);
    }
}
