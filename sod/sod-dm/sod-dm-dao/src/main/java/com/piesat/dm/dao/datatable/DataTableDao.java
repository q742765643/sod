package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.DataTableInfoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataTableDao extends BaseDao<DataTableInfoEntity> {
    @Query(value = "select a from DataTableInfoEntity a,DataClassLogicEntity b where a.id=b.tableId and a.databaseId =?1 and (?2 is null or b.dataClassId = ?2) ")
    List<DataTableInfoEntity> getByDatabaseIdAndClassId(String databaseId, String dataClassId);

    List<DataTableInfoEntity> findByDatabaseId(String databaseId);

    @Query(value = "select a from DataTableInfoEntity a,DataClassLogicEntity b where (a.id=b.tableId or a.id=b.subTableId) and b.id =?1 ")
    List<DataTableInfoEntity> getByClassLogicId(String classLogic);

    @Query(value = "select a from DataTableInfoEntity a,DataClassLogicEntity b where (a.id=b.tableId or a.id=b.subTableId) and b.dataClassId =?1 and a.databaseId = ?2  ")
    List<DataTableInfoEntity> getByclassIdAndDatabaseId(String dataClassId, String databaseId);

    List<DataTableInfoEntity> findByTableName(String tableName);

}
