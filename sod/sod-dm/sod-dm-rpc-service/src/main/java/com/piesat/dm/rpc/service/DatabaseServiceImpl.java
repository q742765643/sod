package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseDao;
import com.piesat.dm.entity.DataTableEntity;
import com.piesat.dm.entity.DatabaseEntity;
import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.dm.rpc.mapper.DatabaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private DatabaseMapper databaseMapper;

    @Override
    public BaseDao<DatabaseEntity> getBaseDao() {
        return databaseDao;
    }

    @Override
    public DatabaseDto saveDto(DatabaseDto databaseDto) {
        DatabaseEntity databaseEntity = this.databaseMapper.toEntity(databaseDto);
        databaseEntity = this.save(databaseEntity);
        return this.databaseMapper.toDto(databaseEntity);
    }

    @Override
    public List<DatabaseDto> all() {
        List<DatabaseEntity> all = this.getAll();
        return this.databaseMapper.toDto(all);
    }

    @Override
    public DatabaseDto getDotById(String id) {
        DatabaseEntity databaseEntity = this.getById(id);
        return this.databaseMapper.toDto(databaseEntity);
    }
}
