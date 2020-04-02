package com.piesat.dm.rpc.service.special;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.special.DatabaseSpecialAuthorityDao;
import com.piesat.dm.entity.special.DatabaseSpecialAuthorityEntity;
import com.piesat.dm.rpc.api.special.DatabaseSpecialAuthorityService;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialAuthorityDto;
import com.piesat.dm.rpc.mapper.special.DatabaseSpecialAuthorityMapper;
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
