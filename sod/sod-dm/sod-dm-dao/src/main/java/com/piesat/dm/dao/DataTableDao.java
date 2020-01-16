package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DataTableEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataTableDao extends BaseDao<DataTableEntity> {
    @Query(value = "select a from DataTableEntity a,DataLogicEntity b where a.classLogic=b.id and b.databaseId =?1 and (?2 is null or b.dataClassId = ?2) ")
    List<DataTableEntity> getByDatabaseIdAndClassId(String databaseId, String dataClassId);

    @Query(value = "select A.* ,B.storage_type from T_SOD_DATA_TABLE A,T_SOD_DATA_LOGIC B where A.class_logic_id=B.id and B.database_id =?1 ", nativeQuery = true)
    List<Map<String, Object>> getByDatabaseId(String databaseId);

    @Query(value = "select a from DataTableEntity a where a.classLogic.id = ?1")
    List<DataTableEntity> getByClassLogicId(String classLogic);

    void deleteByClassLogic_Id(String clId);

    List<DataTableEntity> findByClassLogic_Id(String clId);
}
