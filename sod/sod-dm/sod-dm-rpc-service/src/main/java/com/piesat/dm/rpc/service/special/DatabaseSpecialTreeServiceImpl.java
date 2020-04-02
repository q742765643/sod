package com.piesat.dm.rpc.service.special;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.special.DatabaseSpecialTreeDao;
import com.piesat.dm.entity.special.DatabaseSpecialTreeEntity;
import com.piesat.dm.rpc.api.special.DatabaseSpecialTreeService;
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
