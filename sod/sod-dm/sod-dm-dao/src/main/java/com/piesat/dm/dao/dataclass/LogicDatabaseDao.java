package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.LogicDatabaseEntity;
import com.piesat.dm.entity.dataclass.LogicStorageTypesEntity;
import org.springframework.stereotype.Repository;

/**
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月27日 16:19:34
 */
@Repository
public interface LogicDatabaseDao extends BaseDao<LogicDatabaseEntity> {
    /**
     * 根据数据库id删除关联信息
     * @param databaseId
     */
    void deleteByDatabaseId(String databaseId);
}
