package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataClassDao;
import com.piesat.dm.entity.DataClassEntity;
import com.piesat.dm.rpc.api.DataClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月22日 16:31:15
 */
@Service
public class DataClassServiceImpl extends BaseService<DataClassEntity> implements DataClassService {
    @Autowired
    private DataClassDao dataClassDao;

    @Override
    public BaseDao<DataClassEntity> getBaseDao() {
        return dataClassDao;
    }
}
