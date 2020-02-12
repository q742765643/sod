package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DatabaseSpecialEntity;
import com.piesat.dm.entity.DatabaseUserEntity;
import org.springframework.stereotype.Repository;

/**
 * 专题库管理
 */
@Repository
public interface DatabaseSpecialDao extends BaseDao<DatabaseSpecialEntity> {

}
