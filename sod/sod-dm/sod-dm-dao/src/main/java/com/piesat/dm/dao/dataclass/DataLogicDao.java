package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassAndTableEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DataLogicDao extends BaseDao<DataClassAndTableEntity> {
    @Transactional(rollbackFor = Exception.class)
    @Modifying(clearAutomatically = true)
    @Query(value = "update T_SOD_DATA_LOGIC p set p.database_id =?1 where p.database_id = ?2",nativeQuery = true)
    int updateDatabaseId( String id,  String oid);
    @Transactional(rollbackFor = Exception.class)
    void deleteByDataClassId(String dataClassId);

    List<DataClassAndTableEntity> findByDataClassId(String dataClassId);

    List<DataClassAndTableEntity> findByTableId(String tableId);

    List<DataClassAndTableEntity> findBySubTableId(String tableId);

    List<DataClassAndTableEntity> findByTableIdOrSubTableId(String tableId, String subTableId);
}
