package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataLogicDao;
import com.piesat.dm.entity.DataLogicEntity;
import com.piesat.dm.rpc.api.DataLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资料用途分类
 *
 * @author cwh
 * @date 2019年 11月22日 16:32:48
 */
@Service
public class DataLogicServiceImpl extends BaseService<DataLogicEntity> implements DataLogicService {
    @Autowired
    private DataLogicDao dataLogicDao;

    @Override
    public BaseDao<DataLogicEntity> getBaseDao() {
        return dataLogicDao;
    }
}
