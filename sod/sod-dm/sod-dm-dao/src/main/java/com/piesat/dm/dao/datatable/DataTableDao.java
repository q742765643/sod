package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.DataTableEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface DataTableDao extends BaseDao<DataTableEntity> {
    @Query(value = "select a from DataTableEntity a,DataLogicEntity b where a.classLogic=b.id and b.databaseId =?1 and (?2 is null or b.dataClassId = ?2) ")
    List<DataTableEntity> getByDatabaseIdAndClassId(String databaseId, String dataClassId);

    @Query(value = "select A.* ,B.storage_type from T_SOD_DATA_TABLE A,T_SOD_DATA_LOGIC B where A.class_logic_id=B.id and B.database_id =?1 ", nativeQuery = true)
    List<Map<String, Object>> getByDatabaseId(String databaseId);

    List<DataTableEntity> findByClassLogicId(String classLogic);

    @Transactional
    void deleteByClassLogic_Id(String clId);

    List<DataTableEntity> findByClassLogic_Id(String clId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update T_SOD_DATA_TABLE p set p.table_name =?1  where p.id = ?2",nativeQuery = true)
    int updateById(String table_name, String id);

    List<DataTableEntity> findByDataServiceIdAndClassLogicId(String dataClassId, String databaseId);

    List<DataTableEntity> findByTableNameAndClassLogic_DatabaseIdAndClassLogic_DataClassId(String tableName,String databaseId,String dataclassId);

    List<DataTableEntity> findByTableName(String tableName);

}
