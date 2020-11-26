package com.piesat.schedule.dao.synctofile;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.synctofile.SyncToFileLogEntity;
import org.springframework.stereotype.Repository;

/**
 * @author cwh
 * @date 2020年 10月28日 14:47:32
 */
@Repository
public interface SyncToFileLogDao extends BaseDao<SyncToFileLogEntity> {
}
