package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataLogicEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DataLogicDao extends BaseDao<DataLogicEntity> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update T_SOD_DATA_LOGIC p set p.database_id =?1 where p.database_id = ?2",nativeQuery = true)
    int updateDatabaseId( String id,  String oid);
    @Transactional
    void deleteByDataClassId(String dataClassId);

    List<DataLogicEntity> findByDataClassId(String dataClassId);

    List<DataLogicEntity> findByDataClassIdAndLogicFlagAndStorageType(String dataClassId, String logicFlag, String storageType);

    List<DataLogicEntity> findByDataClassIdAndDatabaseIdAndLogicFlag(String dataClassId,String databaseId,String logicFlag);

	List<DataLogicEntity> findByDatabaseId(String databaseId);

    List<DataLogicEntity> findByDatabaseIdAndDataClassId(String databaseId,String dataclassId);
}
