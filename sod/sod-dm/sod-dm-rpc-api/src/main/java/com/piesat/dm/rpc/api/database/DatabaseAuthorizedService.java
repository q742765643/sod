package com.piesat.dm.rpc.api.database;

import com.piesat.dm.rpc.dto.database.DatabaseAuthorizedDto;

import java.util.List;

/**
 * @author cwh
 * @program: sod
 * @date 2020年 12月21日 11:57:18
 */
public interface DatabaseAuthorizedService {
    DatabaseAuthorizedDto saveDto(DatabaseAuthorizedDto databaseAuthorizedDto);

    DatabaseAuthorizedDto getDotById(String id);

    void delete(String id);

    List<DatabaseAuthorizedDto> all();

    /**
     * 根据数据库用户名查询
     *
     * @param username
     * @return
     */
    List<DatabaseAuthorizedDto> findByDatabaseUsername(String username);

    /**
     * 根据数据库用户和数据库id删除
     * @param username
     * @param databaseId
     */
    void delete(String username, String databaseId);

    /**
     * 根据数据库用户名和数据库id查询
     * @param username
     * @param databaseId
     * @return
     */
    DatabaseAuthorizedDto findByDatabaseUsernameAndDatabaseId(String username, String databaseId);

}
