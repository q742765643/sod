package com.piesat.dm.dao.database;

import com.piesat.common.jpa.BaseDao;
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
    SchemaEntity findByDatabaseDefine(String databaseId);

    /**
     * 根据专题库ID查询
     * @param tdbId
     * @return
     */
    List<SchemaEntity> findByTdbId(String tdbId);

    List<SchemaEntity> findByLevel(int level);

    List<SchemaEntity> findByDatabaseClassifyAndIdIn(String databaseClassify, List<String> ids);

    List<SchemaEntity> findByIdIn(List<String> ids);

    List<SchemaEntity> findByDatabaseClassifyAndDatabaseDefineIdIn(String databaseClassify, List<String> databaseDefineIds);

    List<SchemaEntity> findByDatabaseDefineIdIn(List<String> databaseDefineIds);

    List<SchemaEntity> findByDatabaseClassify(String databaseClassify);

    List<SchemaEntity> findByDatabaseDefine_Id(String id);

    List<SchemaEntity> findByDatabaseClassifyAndDatabaseDefineId(String databaseClassify, String databaseDefineId);

    List<SchemaEntity> findByDatabaseClassifyAndDatabaseNameAndSchemaNameAndDatabaseDefineId(String databaseClassify, String databaseName, String schemaName, String databaseDefineId);

    void deleteByDatabaseDefine_Id(String id);

    List<SchemaEntity> findByDatabaseDefine_IdAndDatabaseName(String id, String databaseName);


    List<SchemaEntity> findByDatabaseDefine_UserDisplayControl(int use);

    void deleteByTdbId(String sdbId);
}
