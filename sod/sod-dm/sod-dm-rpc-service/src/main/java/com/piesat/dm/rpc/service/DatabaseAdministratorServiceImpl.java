package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseAdministratorDao;
import com.piesat.dm.entity.DatabaseAdministratorEntity;
import com.piesat.dm.rpc.api.DatabaseAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据库管理账户
 *
 * @author cwh
 * @date 2019年 11月22日 15:56:39
 */
@Service
public class DatabaseAdministratorServiceImpl extends BaseService<DatabaseAdministratorEntity> implements DatabaseAdministratorService {
    @Autowired
    private DatabaseAdministratorDao databaseAdministratorDao;

    @Override
    public BaseDao<DatabaseAdministratorEntity> getBaseDao() {
        return databaseAdministratorDao;
    }
}
