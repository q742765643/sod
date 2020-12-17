package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassLogicEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DataLogicDao extends BaseDao<DataClassLogicEntity> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update T_SOD_DATA_LOGIC p set p.database_id =?1 where p.database_id = ?2",nativeQuery = true)
    int updateDatabaseId( String id,  String oid);
    @Transactional
    void deleteByDataClassId(String dataClassId);

    List<DataClassLogicEntity> findByDataClassId(String dataClassId);

    List<DataClassLogicEntity> findByTableId(String tableId);

    List<DataClassLogicEntity> findBySubTableId(String tableId);

    List<DataClassLogicEntity> findByTableIdOrSubTableId(String tableId,String subTableId);
}
