package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataTableDao;
import com.piesat.dm.entity.DataTableEntity;
import com.piesat.dm.rpc.api.DataTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月22日 16:34:17
 */
@Service
public class DataTableServiceImpl extends BaseService<DataTableEntity> implements DataTableService {
    @Autowired
    private DataTableDao dataTableDao;

    @Override
    public BaseDao<DataTableEntity> getBaseDao() {
        return dataTableDao;
    }
}
