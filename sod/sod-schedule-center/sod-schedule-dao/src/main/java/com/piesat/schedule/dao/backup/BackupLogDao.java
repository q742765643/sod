package com.piesat.schedule.dao.backup;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import org.springframework.stereotype.Repository;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-29 19:52
 **/
@Repository
public interface BackupLogDao extends BaseDao<BackupLogEntity> {
}

