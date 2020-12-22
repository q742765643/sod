package com.piesat.dm.dao.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.database.DatabaseUserEntity;
import com.piesat.dm.entity.database.DbUserAlterLogEntity;

/**
 * @author cwh
 * @date 2020年 12月16日 15:18:37
 */
public interface DbUserAlterLogDao extends BaseDao<DbUserAlterLogEntity> {

    /**
     * 删除
     * @param id
     */
    void deleteByAuthorizeId(String id);
}
