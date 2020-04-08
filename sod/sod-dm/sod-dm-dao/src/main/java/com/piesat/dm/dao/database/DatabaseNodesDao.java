package com.piesat.dm.dao.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.database.DatabaseNodesEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseNodesDao extends BaseDao<DatabaseNodesEntity> {
    List<DatabaseNodesEntity> findByDatabaseId(String dataBaseId);
}
