package com.piesat.dm.dao.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.database.DatabaseUserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseUserDao extends BaseDao<DatabaseUserEntity> {
    DatabaseUserEntity findByDatabaseUpId(String databaseUPId);
    /**
     * 获取申请没有被拒绝的用户申请信息
     * @param userId
     * @return
     */
    DatabaseUserEntity findByUserIdAndExamineStatusNot(String userId,String ExamineStatus);

    DatabaseUserEntity findByUserIdAndExamineStatus(String userId,String examineStatus);

    List<DatabaseUserEntity> findByUserId(String userId);

    List<DatabaseUserEntity> findByUserIdAndDatabaseUpId(String userId,String upId);

    List<DatabaseUserEntity> findByExamineStatus(String examineStatus);
}
