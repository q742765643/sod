package com.piesat.dm.dao.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.database.DatabaseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseDao extends BaseDao<DatabaseEntity> {
    /**
     * 根据物理库ID查询
     * @param databaseId
     * @return
     */
    DatabaseEntity findByDatabaseDefine(String databaseId);

    /**
     * 根据专题库ID查询
     * @param tdbId
     * @return
     */
    List<DatabaseEntity> findByTdbId(String tdbId);

    List<DatabaseEntity> findByLevel(int level);

    List<DatabaseEntity> findByDatabaseClassifyAndIdIn(String databaseClassify,List<String> ids);

    List<DatabaseEntity> findByDatabaseClassify(String databaseClassify);

    List<DatabaseEntity> findByDatabaseDefine_Id(String id);

    List<DatabaseEntity> findByDatabaseClassifyAndDatabaseDefineId(String databaseClassify,String databaseDefineId);

    List<DatabaseEntity> findByDatabaseClassifyAndDatabaseNameAndSchemaNameAndDatabaseDefineId(String databaseClassify,String databaseName,String schemaName,String databaseDefineId);

    void deleteByDatabaseDefine_Id(String id);
}
