package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseDefineDao;
import com.piesat.dm.entity.DatabaseDefineEntity;
import com.piesat.dm.rpc.api.DatabaseDefineService;
import com.piesat.dm.rpc.dto.DatabaseDefineDto;
import com.piesat.dm.rpc.mapper.DatabaseDefineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月22日 15:59:29
 */
@Service
public class DatabaseDefineServiceImpl extends BaseService<DatabaseDefineEntity> implements DatabaseDefineService {
    @Autowired
    private DatabaseDefineDao databaseDefineDao;
    @Autowired
    private DatabaseDefineMapper databaseDefineMapper;

    @Override
    public BaseDao<DatabaseDefineEntity> getBaseDao() {
        return databaseDefineDao;
    }

    @Override
    public DatabaseDefineDto saveDto(DatabaseDefineDto databaseDefineDto) {
        DatabaseDefineEntity databaseDefineEntity = this.databaseDefineMapper.toEntity(databaseDefineDto);
        DatabaseDefineEntity save = this.save(databaseDefineEntity);
        return this.databaseDefineMapper.toDto(save);
    }

    @Override
    public List<DatabaseDefineDto> all() {
        List<DatabaseDefineEntity> all = this.getAll();
        return this.databaseDefineMapper.toDto(all);
    }

    @Override
    public DatabaseDefineDto getDotById(String id) {
        DatabaseDefineEntity databaseDefineEntity = this.getById(id);
        return this.databaseDefineMapper.toDto(databaseDefineEntity);
    }
}
