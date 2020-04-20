package com.piesat.dm.dao.special;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.special.DatabaseSpecialAccessEntity;
import com.piesat.dm.entity.special.DatabaseSpecialEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/11 21:30
 */
@Repository
public interface DatabaseSpecialAccessDao extends BaseDao<DatabaseSpecialAccessEntity> {
    DatabaseSpecialAccessEntity findBySdbIdAndUserId(String sdbId, String userId);
}
