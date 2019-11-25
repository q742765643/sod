package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseDefineDao;
import com.piesat.dm.entity.DatabaseDefineEntity;
import com.piesat.dm.rpc.api.DatabaseDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据库定义
 *
 * @author cwh
 * @date 2019年 11月22日 15:59:29
 */
@Service
public class DatabaseDefineServiceImpl extends BaseService<DatabaseDefineEntity> implements DatabaseDefineService {
    @Autowired
    private DatabaseDefineDao databaseDefineDao;

    @Override
    public BaseDao<DatabaseDefineEntity> getBaseDao() {
        return databaseDefineDao;
    }
}
