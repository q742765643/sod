package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseNodesDao;
import com.piesat.dm.entity.DatabaseNodesEntity;
import com.piesat.dm.rpc.api.DatabaseNodesService;
import com.piesat.dm.rpc.dto.DatabaseNodesDto;
import com.piesat.dm.rpc.mapper.DatabaseNodesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据库节点信息
 *
 * @author cwh
 * @date 2019年 11月22日 16:25:12
 */
@Service
public class DatabaseNodesServiceImpl extends BaseService<DatabaseNodesEntity> implements DatabaseNodesService {
    @Autowired
    private DatabaseNodesDao databaseNodesDao;
    @Autowired
    private DatabaseNodesMapper databaseNodesMapper;

    @Override
    public BaseDao<DatabaseNodesEntity> getBaseDao() {
        return databaseNodesDao;
    }

    @Override
    public DatabaseNodesDto saveDto(DatabaseNodesDto databaseNodesDto) {
        DatabaseNodesEntity databaseNodesEntity = this.databaseNodesMapper.toEntity(databaseNodesDto);
        databaseNodesEntity = this.save(databaseNodesEntity);
        return this.databaseNodesMapper.toDto(databaseNodesEntity);
    }

    @Override
    public List<DatabaseNodesDto> all() {
        List<DatabaseNodesEntity> all = this.getAll();
        this.delete();
        return this.databaseNodesMapper.toDto(all);
    }

    @Override
    public DatabaseNodesDto getDotById(String id) {
        DatabaseNodesEntity databaseNodesEntity = this.getById(id);
        return this.databaseNodesMapper.toDto(databaseNodesEntity);
    }
}
