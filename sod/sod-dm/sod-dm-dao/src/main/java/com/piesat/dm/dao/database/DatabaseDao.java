package com.piesat.dm.dao.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.database.DatabaseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseDao extends BaseDao<DatabaseEntity> {
    List<DatabaseEntity> findByDatabaseType(String databaseType);

    List<DatabaseEntity> findByIdIn(List<String> ids);

    List<DatabaseEntity> findByUserDisplayControlIn(List<String> paramList);

    List<DatabaseEntity> findByUserDisplayControlNot(int i);
}
