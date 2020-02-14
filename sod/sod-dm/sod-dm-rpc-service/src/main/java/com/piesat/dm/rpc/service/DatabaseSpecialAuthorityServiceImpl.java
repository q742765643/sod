package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseSpecialAuthorityDao;
import com.piesat.dm.dao.DatabaseSpecialDao;
import com.piesat.dm.entity.DatabaseSpecialAuthorityEntity;
import com.piesat.dm.entity.DatabaseSpecialEntity;
import com.piesat.dm.entity.DatabaseSpecialReadWriteEntity;
import com.piesat.dm.rpc.api.DatabaseSpecialAuthorityService;
import com.piesat.dm.rpc.api.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.DatabaseSpecialAuthorityDto;
import com.piesat.dm.rpc.dto.DatabaseSpecialDto;
import com.piesat.dm.rpc.mapper.DatabaseSpecialAuthorityMapper;
import com.piesat.dm.rpc.mapper.DatabaseSpecialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专题库管理
 */
@Service
public class DatabaseSpecialAuthorityServiceImpl extends BaseService<DatabaseSpecialAuthorityEntity> implements DatabaseSpecialAuthorityService {
    @Autowired
    private DatabaseSpecialAuthorityDao databaseSpecialAuthorityDao;
    @Autowired
    private DatabaseSpecialAuthorityMapper databaseSpecialAuthorityMapper;

    @Override
    public BaseDao<DatabaseSpecialAuthorityEntity> getBaseDao() {
        return databaseSpecialAuthorityDao;
    }


    @Override
    public List<DatabaseSpecialAuthorityDto> getAuthorityBySdbId(String sdbId) {
        List<DatabaseSpecialAuthorityEntity> dataList = databaseSpecialAuthorityDao.findBySdbId(sdbId);
        return this.databaseSpecialAuthorityMapper.toDto(dataList);
    }
}
