package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.TableColumnDao;
import com.piesat.dm.entity.TableColumnEntity;
import com.piesat.dm.rpc.api.TableColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月22日 16:39:16
 */
@Service
public class TableColumnServiceImpl extends BaseService<TableColumnEntity> implements TableColumnService {
    @Autowired
    private TableColumnDao tableColumnDao;

    @Override
    public BaseDao<TableColumnEntity> getBaseDao() {
        return tableColumnDao;
    }
}
