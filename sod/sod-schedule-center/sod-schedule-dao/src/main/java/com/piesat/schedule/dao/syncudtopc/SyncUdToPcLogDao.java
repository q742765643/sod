package com.piesat.schedule.dao.syncudtopc;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcLogEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cwh
 * @date 2020年 10月28日 14:47:32
 */
@Repository
public interface SyncUdToPcLogDao extends BaseDao<SyncUdToPcLogEntity> {
    /**
     *
     * @return
     */
    @Query(value = "SELECT * FROM SyncUdToPcLogEntity  WHERE jobId =?1 ORDER BY createTime DESC LIMIT 1",nativeQuery = true)
    SyncUdToPcLogEntity getLatest(String jobId);

}
