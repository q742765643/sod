package com.piesat.dm.dao.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.database.SchemaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemaDao extends BaseDao<SchemaEntity> {
    /**
     * 根据物理库ID查询
     * @param databaseId
     * @return
     */
    SchemaEntity findByDatabase(String databaseId);

    /**
     * 根据专题库ID查询
     * @param tdbId
     * @return
     */
    List<SchemaEntity> findByTdbId(String tdbId);

    List<SchemaEntity> findByLevel(int level);

    List<SchemaEntity> findByDatabaseClassifyAndIdIn(String databaseClassify, List<String> ids);

    List<SchemaEntity> findByIdIn(List<String> ids);

    List<SchemaEntity> findByDatabaseClassifyAndDatabaseIdIn(String databaseClassify, List<String> databaseDefineIds);

    List<SchemaEntity> findByDatabaseIdIn(List<String> databaseDefineIds);

    List<SchemaEntity> findByDatabaseClassify(String databaseClassify);

    List<SchemaEntity> findByDatabase_Id(String id);

    List<SchemaEntity> findByDatabaseClassifyAndDatabaseId(String databaseClassify, String databaseDefineId);

    List<SchemaEntity> findByDatabaseClassifyAndSchemaNameCnAndSchemaNameAndDatabaseId(String databaseClassify, String databaseName, String schemaName, String databaseDefineId);

    void deleteByDatabase_Id(String id);

    List<SchemaEntity> findByDatabase_IdAndSchemaNameCn(String id, String databaseName);

    List<SchemaEntity> findByDatabase_UserDisplayControl(int use);

    void deleteByTdbId(String sdbId);

    List<SchemaEntity> findBySchemaNameCn(String databaseName);
}
