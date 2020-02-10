package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DatabaseUserEntity;
import com.piesat.dm.entity.LogicStorageTypesEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseUserDao extends BaseDao<DatabaseUserEntity> {
}
