package com.piesat.schedule.dao.syncdar;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.syncdar.SyncDarEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author cwh
 * @date 2020年 10月28日 14:40:38
 */
@Repository
public interface SyncDarDao extends BaseDao<SyncDarEntity> {
    /**
     * 更新最后同步时间
     * @param lastTime
     * @param id
     * @return
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update SyncDarEntity p set p.lastTime =?1 where p.id = ?2")
    int updateLastTime(Date lastTime, String id);
}
