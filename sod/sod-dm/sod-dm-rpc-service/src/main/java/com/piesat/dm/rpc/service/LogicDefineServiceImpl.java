package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.LogicDefineDao;
import com.piesat.dm.entity.LogicDefineEntity;
import com.piesat.dm.rpc.api.LogicDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月22日 16:36:19
 */
@Service
public class LogicDefineServiceImpl extends BaseService<LogicDefineEntity> implements LogicDefineService {
    @Autowired
    private LogicDefineDao logicDefineDao;

    @Override
    public BaseDao<LogicDefineEntity> getBaseDao() {
        return logicDefineDao;
    }
}
