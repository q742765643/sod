package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseDao;
import com.piesat.dm.entity.DatabaseEntity;
import com.piesat.dm.rpc.api.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据库基础库专题库
 *
 * @author cwh
 * @date 2019年 11月22日 16:27:41
 */
@Service
public class DatabaseServiceImpl extends BaseService<DatabaseEntity> implements DatabaseService {
    @Autowired
    private DatabaseDao databaseDao;

    @Override
    public BaseDao<DatabaseEntity> getBaseDao() {
        return databaseDao;
    }
}
