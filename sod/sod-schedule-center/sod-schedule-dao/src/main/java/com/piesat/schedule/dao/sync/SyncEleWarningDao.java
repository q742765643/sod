package com.piesat.schedule.dao.sync;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.sync.SyncConfigEntity;
import com.piesat.schedule.entity.sync.SyncEleWarningEntity;
import org.springframework.stereotype.Repository;

/**
 * 键表要素表监控
 *
 * @author cwh
 * @date 2020年 05月11日 13:45:02
 */
@Repository
public interface SyncEleWarningDao extends BaseDao<SyncEleWarningEntity> {
}
