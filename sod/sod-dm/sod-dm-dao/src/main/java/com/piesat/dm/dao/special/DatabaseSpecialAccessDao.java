package com.piesat.dm.dao.special;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.special.DatabaseSpecialAccessEntity;
import com.piesat.dm.entity.special.DatabaseSpecialEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author yaya
 * @description TODO
 * @date 2020/4/11 21:30
 */
@Repository
public interface DatabaseSpecialAccessDao extends BaseDao<DatabaseSpecialAccessEntity> {
    DatabaseSpecialAccessEntity findBySdbIdAndUserId(String sdbId, String userId);
    @Transactional(rollbackFor = Exception.class)
    void deleteBySdbIdAndUserId(String sdbId,String userId);
}
