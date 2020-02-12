package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DatabaseUserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseUserDao extends BaseDao<DatabaseUserEntity> {
    DatabaseUserEntity findByDatabaseUpId(String databaseUPId);
    /**
     * 获取申请没有被拒绝的用户申请信息
     * @param userId
     * @return
     */
    DatabaseUserEntity findByUserIdAndExamineStatusNot(String userId,String ExamineStatus);
}
