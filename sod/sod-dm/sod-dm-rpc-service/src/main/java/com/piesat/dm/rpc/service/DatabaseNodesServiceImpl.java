package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseNodesDao;
import com.piesat.dm.entity.DatabaseNodesEntity;
import com.piesat.dm.rpc.api.DatabaseNodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据库节点信息
 *
 * @author cwh
 * @date 2019年 11月22日 16:25:12
 */
@Service
public class DatabaseNodesServiceImpl extends BaseService<DatabaseNodesEntity> implements DatabaseNodesService {
    @Autowired
    private DatabaseNodesDao databaseNodesDao;

    @Override
    public BaseDao<DatabaseNodesEntity> getBaseDao() {
        return databaseNodesDao;
    }
}
