package com.piesat.dm.dao.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.database.DatabaseDefineEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseDefineDao extends BaseDao<DatabaseDefineEntity> {
    List<DatabaseDefineEntity> findByDatabaseType(String databaseType);

    List<DatabaseDefineEntity> findByIdIn(List<String> ids);

    List<DatabaseDefineEntity> findByUserDisplayControlIn(List<String> paramList);

    List<DatabaseDefineEntity> findByUserDisplayControlNot(int i);
}
