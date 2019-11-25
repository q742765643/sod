package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.LogicStorageTypesDao;
import com.piesat.dm.entity.LogicStorageTypesEntity;
import com.piesat.dm.rpc.api.LogicStorageTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据用途与存储类型对应关系
 *
 * @author cwh
 * @date 2019年 11月22日 16:37:47
 */
@Service
public class LogicStorageTypesServiceImpl extends BaseService<LogicStorageTypesEntity> implements LogicStorageTypesService {
    @Autowired
    private LogicStorageTypesDao logicStorageTypesDao;

    @Override
    public BaseDao<LogicStorageTypesEntity> getBaseDao() {
        return logicStorageTypesDao;
    }
}
