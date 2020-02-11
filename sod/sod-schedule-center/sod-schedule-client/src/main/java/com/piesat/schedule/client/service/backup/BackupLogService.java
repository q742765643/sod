package com.piesat.schedule.client.service.backup;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.backup.BackupLogDao;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.mapper.backup.BackupLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-06 14:11
 **/
@Service
public class BackupLogService extends BaseService<BackupLogEntity>{
    @Autowired
    private BackupLogDao backupLogDao;
    @Autowired
    private BackupLogMapper backupLogMapper;
    @Override
    public BaseDao<BackupLogEntity> getBaseDao() {
        return backupLogDao;
    }

    

}

