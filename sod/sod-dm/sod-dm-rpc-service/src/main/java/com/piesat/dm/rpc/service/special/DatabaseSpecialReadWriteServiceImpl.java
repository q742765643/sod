package com.piesat.dm.rpc.service.special;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.special.DatabaseSpecialReadWriteDao;
import com.piesat.dm.entity.special.DatabaseSpecialReadWriteEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.mapper.special.DatabaseSpecialReadWriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 专题库管理
 */
@Service
public class DatabaseSpecialReadWriteServiceImpl extends BaseService<DatabaseSpecialReadWriteEntity> implements DatabaseSpecialReadWriteService {
    @Autowired
    private DatabaseSpecialReadWriteDao databaseSpecialReadWriteDao;
    @Autowired
    private DatabaseSpecialReadWriteMapper databaseSpecialReadWriteMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;

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

    @Override
    public List<Map<String, Object>> getRecordSpecialByTdbId(String sdbId, String userId) {
        return mybatisQueryMapper.getRecordSpecialByTdbId(sdbId,userId);
    }

}
