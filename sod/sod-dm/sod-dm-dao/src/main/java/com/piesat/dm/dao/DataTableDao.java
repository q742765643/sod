package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DataTableEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataTableDao extends BaseDao<DataTableEntity> {
    @Query(value="select a from DataTableEntity a,DataLogicEntity b where a.classLogicId=b.id and b.databaseId =?1 and b.dataClassId = ?2")
    List<DataTableEntity> getByDatabaseIdAndClassId(String databaseId,String dataClassId);
}
