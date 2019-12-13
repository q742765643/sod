package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.LogicStorageTypesDao;
import com.piesat.dm.entity.LogicStorageTypesEntity;
import com.piesat.dm.rpc.api.LogicStorageTypesService;
import com.piesat.dm.rpc.dto.LogicStorageTypesDto;
import com.piesat.dm.rpc.mapper.LogicStorageTypesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据用途与存储类型对应关系
 *
 * @author cwh
 * @date 2019年 11月22日 16:37:47
 */
@Service
public class LogicStorageTypesServiceImpl extends BaseService<LogicStorageTypesEntity> implements LogicStorageTypesService {
    @Autowired
    private LogicStorageTypesDao logicStorageTypesDao;
    @Autowired
    private LogicStorageTypesMapper logicStorageTypesMapper;

    @Override
    public BaseDao<LogicStorageTypesEntity> getBaseDao() {
        return logicStorageTypesDao;
    }

    @Override
    public LogicStorageTypesDto saveDto(LogicStorageTypesDto logicStorageTypesDto) {
        LogicStorageTypesEntity logicStorageTypesEntity = this.logicStorageTypesMapper.toEntity(logicStorageTypesDto);
        logicStorageTypesEntity = this.save(logicStorageTypesEntity);
        return this.logicStorageTypesMapper.toDto(logicStorageTypesEntity);
    }

    @Override
    public List<LogicStorageTypesDto> all() {
        List<LogicStorageTypesEntity> all = this.getAll();
        return this.logicStorageTypesMapper.toDto(all);
    }

    @Override
    public LogicStorageTypesDto getDotById(String id) {
        LogicStorageTypesEntity logicStorageTypesEntity = this.getById(id);
        return this.logicStorageTypesMapper.toDto(logicStorageTypesEntity);
    }
}
