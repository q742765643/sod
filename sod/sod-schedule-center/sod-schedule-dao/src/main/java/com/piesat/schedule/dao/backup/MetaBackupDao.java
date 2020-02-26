package com.piesat.schedule.dao.backup;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import org.springframework.stereotype.Repository;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-26 15:54
 **/
@Repository
public interface MetaBackupDao extends BaseDao<MetaBackupEntity> {
}

