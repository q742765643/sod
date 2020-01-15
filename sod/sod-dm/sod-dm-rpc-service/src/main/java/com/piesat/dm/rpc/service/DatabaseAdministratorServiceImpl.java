package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.core.parser.ColumnSet;
import com.piesat.dm.dao.DatabaseAdministratorDao;
import com.piesat.dm.entity.DatabaseAdministratorEntity;
import com.piesat.dm.rpc.api.DatabaseAdministratorService;
import com.piesat.dm.rpc.dto.DatabaseAdministratorDto;
import com.piesat.dm.rpc.mapper.DatabaseAdministratorMapper;
import com.piesat.dm.rpc.mapper.DatabaseDefineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 数据库管理账户
 *
 * @author cwh
 * @date 2019年 11月22日 15:56:39
 */
@Service
public class DatabaseAdministratorServiceImpl extends BaseService<DatabaseAdministratorEntity> implements DatabaseAdministratorService {
    @Autowired
    private DatabaseAdministratorDao databaseAdministratorDao;
    @Autowired
    private DatabaseAdministratorMapper databaseAdministratorMapper;
    @Autowired
    private DatabaseDefineMapper databaseDefineMapper;
    @Autowired
    private ColumnSet columnSet;

    @Override
    public BaseDao<DatabaseAdministratorEntity> getBaseDao() {
        return databaseAdministratorDao;
    }

    /**
     * 查询管理账户
     * @param databaseAdministratorDto
     * @return
     */
    @Override
    public List<DatabaseAdministratorDto> getByExample(DatabaseAdministratorDto databaseAdministratorDto) {
        DatabaseAdministratorEntity databaseAdministratorEntity = this.databaseAdministratorMapper.toEntity(databaseAdministratorDto);
        Example<DatabaseAdministratorEntity> example = Example.of(databaseAdministratorEntity);
        List<DatabaseAdministratorEntity> all = this.databaseAdministratorDao.findAll(example);
        return this.databaseAdministratorMapper.toDto(all);
    }

    @Override
    public DatabaseAdministratorDto getDotById(String id) {
        DatabaseAdministratorEntity databaseAdministratorEntity = this.databaseAdministratorDao.getOne(id);
        return this.databaseAdministratorMapper.toDto(databaseAdministratorEntity);
    }

    @Override
    public List<DatabaseAdministratorDto> all() {
        Map<String, Object> cassandra = columnSet.getCassandra();
        Map<String, Object> gbase8a = columnSet.getGbase8a();
        Map<String, Object> xugu = columnSet.getXugu();
        System.out.println(xugu.size());
        List<DatabaseAdministratorEntity> all = this.getAll();
        return this.databaseAdministratorMapper.toDto(all);
    }

    @Override
    public DatabaseAdministratorDto saveDto(DatabaseAdministratorDto databaseAdministratorDto) {
        DatabaseAdministratorEntity databaseAdministratorEntity = this.databaseAdministratorMapper.toEntity(databaseAdministratorDto);
        databaseAdministratorEntity = this.save(databaseAdministratorEntity);
        return this.databaseAdministratorMapper.toDto(databaseAdministratorEntity);
    }

}
