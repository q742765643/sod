package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Cassandra;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.DatabaseDao;
import com.piesat.dm.dao.DatabaseDefineDao;
import com.piesat.dm.dao.DatabaseSpecialDao;
import com.piesat.dm.dao.DatabaseUserDao;
import com.piesat.dm.entity.*;
import com.piesat.dm.rpc.api.DatabaseSpecialService;
import com.piesat.dm.rpc.api.DatabaseUserService;
import com.piesat.dm.rpc.dto.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.DatabaseUserDto;
import com.piesat.dm.rpc.mapper.DatabaseSpecialMapper;
import com.piesat.dm.rpc.mapper.DatabaseUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 专题库管理
 */
@Service
public class DatabaseSpecialServiceImpl extends BaseService<DatabaseSpecialEntity> implements DatabaseSpecialService {
    @Autowired
    private DatabaseSpecialDao databaseSpecialDao;
    @Autowired
    private DatabaseSpecialMapper databaseSpecialMapper;

    @Override
    public BaseDao<DatabaseSpecialEntity> getBaseDao() {
        return databaseSpecialDao;
    }

    @Override
    public List<DatabaseSpecialDto> all() {
        List<DatabaseSpecialEntity> all = this.getAll();
        return this.databaseSpecialMapper.toDto(all);
    }

    @Override
    public DatabaseSpecialDto getDotById(String id) {
        DatabaseSpecialEntity databaseSpecialEntity = this.getById(id);
        return this.databaseSpecialMapper.toDto(databaseSpecialEntity);
    }
}
