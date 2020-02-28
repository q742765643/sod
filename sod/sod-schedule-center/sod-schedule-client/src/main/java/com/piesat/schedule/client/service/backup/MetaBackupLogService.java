package com.piesat.schedule.client.service.backup;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.backup.MetaBackupLogDao;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-28 10:50
 **/
@Service
public class MetaBackupLogService extends BaseService<MetaBackupLogEntity>{
    @Autowired
    private MetaBackupLogDao metaBackupLogDao;
    @Override
    public BaseDao<MetaBackupLogEntity> getBaseDao() {
        return metaBackupLogDao;
    }
}

