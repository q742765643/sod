package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseSpecialTreeDao;
import com.piesat.dm.entity.DatabaseSpecialTreeEntity;
import com.piesat.dm.rpc.api.DatabaseSpecialTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/30 9:47
 */
@Service
public class DatabaseSpecialTreeServiceImpl extends BaseService<DatabaseSpecialTreeEntity> implements DatabaseSpecialTreeService {

    @Autowired
    private DatabaseSpecialTreeDao databaseSpecialTreeDao;

    @Override
    public BaseDao<DatabaseSpecialTreeEntity> getBaseDao() {
        return databaseSpecialTreeDao;
    }
}
