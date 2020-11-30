package com.piesat.schedule.dao.synctofile;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.synctofile.SyncToFileEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author cwh
 * @date 2020年 10月28日 14:47:32
 */
@Repository
public interface SyncToFileDao extends BaseDao<SyncToFileEntity> {
    /**
     * 更新最后同步时间
     * @param lastTime
     * @param id
     * @return
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update SyncToFileEntity p set p.lastTime =?1 where p.id = ?2")
    int updateLastTime(Date lastTime, String id);
}
