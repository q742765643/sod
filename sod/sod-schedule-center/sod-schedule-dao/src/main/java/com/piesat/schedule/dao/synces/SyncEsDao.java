package com.piesat.schedule.dao.synces;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.synces.SyncEsEntity;
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
public interface SyncEsDao extends BaseDao<SyncEsEntity> {
    /**
     * 更新最后同步时间
     * @param lastTime
     * @param id
     * @return
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update SyncEsEntity p set p.lastTime =?1 where p.id = ?2",nativeQuery = true)
    int updateLastTime(Date lastTime, String id);
}
