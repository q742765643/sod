package com.piesat.schedule.dao.sync;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.sync.SyncTaskEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/13 16:52
 */
@Repository
public interface SyncTaskDao extends BaseDao<SyncTaskEntity> {

    public List<SyncTaskEntity> findAllByTargetDatabaseIdAndSlaveTablesIsLike(String targetDatabaseId,String slaveTables);

    public SyncTaskEntity findBySourceTable(String sourceTable);
}
