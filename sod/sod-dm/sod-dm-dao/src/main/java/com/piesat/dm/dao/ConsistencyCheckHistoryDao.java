package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.ConsistencyCheckHistoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/16 15:56
 */
@Repository
public interface ConsistencyCheckHistoryDao extends BaseDao<ConsistencyCheckHistoryEntity> {
    List<ConsistencyCheckHistoryEntity> findByDatabaseIdAndFileName(String databaseId,String fileName);
}
