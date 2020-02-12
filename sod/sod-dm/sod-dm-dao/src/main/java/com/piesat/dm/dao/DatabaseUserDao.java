package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DatabaseUserEntity;
import com.piesat.dm.entity.LogicStorageTypesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseUserDao extends BaseDao<DatabaseUserEntity> {
    @Query(value = "select * from t_sod_database_user WHERE database_up_id='?1'",nativeQuery = true)
    DatabaseUserEntity getByUpId(String databaseUPId);

    /**
     * 获取申请没有被拒绝的用户申请信息
     * @param userId
     * @return
     */
    @Query(value = "select * from t_sod_database_user WHERE examine_status!=2 and user_id='?1'",nativeQuery = true)
    DatabaseUserEntity getByUserId(String userId);
}
