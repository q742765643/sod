package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DatabaseDefineEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseDefineDao extends BaseDao<DatabaseDefineEntity> {
    List<DatabaseDefineEntity> findByDatabaseType(String databaseType);
}
