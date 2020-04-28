package com.piesat.schedule.dao.backup;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-23 09:16
 **/
@Repository
public interface BackupDao extends BaseDao<BackupEntity>{
    List<BackupEntity> findByDatabaseIdAndDataClassId(String databaseId,String dataClassId);
}

