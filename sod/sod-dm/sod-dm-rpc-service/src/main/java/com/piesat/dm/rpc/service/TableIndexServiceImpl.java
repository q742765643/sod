package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.TableIndexDao;
import com.piesat.dm.entity.TableIndexEntity;
import com.piesat.dm.rpc.api.TableIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 表索引
 *
 * @author cwh
 * @date 2019年 11月22日 16:40:38
 */
@Service
public class TableIndexServiceImpl extends BaseService<TableIndexEntity> implements TableIndexService {
    @Autowired
    private TableIndexDao tableIndexDao;

    @Override
    public BaseDao<TableIndexEntity> getBaseDao() {
        return tableIndexDao;
    }
}
