package com.piesat.dm.dao.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.database.DatabaseAuthorizedEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DatabaseAuthorizedDao extends BaseDao<DatabaseAuthorizedEntity> {
    /**
     * 根据数据库用户id查询
     *
     * @param username
     * @return
     */
    List<DatabaseAuthorizedEntity> findByDbUsername(String username);

    /**
     * 根据数据库账户和数据库id删除
     * @param username
     * @param databaseId
     */
    void deleteByDbUsernameAndDatabaseId(String username, String databaseId);

    /**
     * 根据数据库账户和数据库id查询
     * @param username
     * @param databaseId
     * @return
     */
    List<DatabaseAuthorizedEntity> findByDbUsernameAndDatabaseId(String username, String databaseId);

}
