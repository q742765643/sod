package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseSpecialDao;
import com.piesat.dm.dao.DatabaseSpecialReadWriteDao;
import com.piesat.dm.entity.DatabaseSpecialEntity;
import com.piesat.dm.entity.DatabaseSpecialReadWriteEntity;
import com.piesat.dm.rpc.api.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.mapper.DatabaseSpecialMapper;
import com.piesat.dm.rpc.mapper.DatabaseSpecialReadWriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专题库管理
 */
@Service
public class DatabaseSpecialReadWriteServiceImpl extends BaseService<DatabaseSpecialReadWriteEntity> implements DatabaseSpecialReadWriteService {
    @Autowired
    private DatabaseSpecialReadWriteDao databaseSpecialReadWriteDao;
    @Autowired
    private DatabaseSpecialReadWriteMapper databaseSpecialReadWriteMapper;

    @Override
    public BaseDao<DatabaseSpecialReadWriteEntity> getBaseDao() {
        return databaseSpecialReadWriteDao;
    }

    @Override
    public List<DatabaseSpecialReadWriteDto> getDotList(String sdbId, String dataType) {
        List<DatabaseSpecialReadWriteEntity> dataList = databaseSpecialReadWriteDao.findBySdbIdAndDataType(sdbId,dataType);
        return this.databaseSpecialReadWriteMapper.toDto(dataList);
    }

    @Override
    public List<DatabaseSpecialReadWriteDto> findBySdbIdAndDataClassId(String sdbId, String dataClassId) {
        List<DatabaseSpecialReadWriteEntity> readWriteEntities = databaseSpecialReadWriteDao.findBySdbIdAndDataClassId(sdbId, dataClassId);
        return this.databaseSpecialReadWriteMapper.toDto(readWriteEntities);
    }

    @Override
    public DatabaseSpecialReadWriteDto saveDto(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        DatabaseSpecialReadWriteEntity databaseSpecialReadWriteEntity=databaseSpecialReadWriteMapper.toEntity(databaseSpecialReadWriteDto);
        databaseSpecialReadWriteEntity = this.saveNotNull(databaseSpecialReadWriteEntity);
        return databaseSpecialReadWriteMapper.toDto(databaseSpecialReadWriteEntity);
    }

}
