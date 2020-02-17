package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataOnlineTimeDao;
import com.piesat.dm.entity.DataOnlineTimeEntity;
import com.piesat.dm.rpc.api.DataOnlineTimeService;
import com.piesat.dm.rpc.dto.DataOnlineTimeDto;
import com.piesat.dm.rpc.mapper.DataOnlineTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/17 19:22
 */
@Service
public class DataOnlineTimeServiceImpl extends BaseService<DataOnlineTimeEntity> implements DataOnlineTimeService {

    @Autowired
    private DataOnlineTimeDao dataOnlineTimeDao;

    @Autowired
    private DataOnlineTimeMapper dataOnlineTimeMapper;

    @Override
    public BaseDao<DataOnlineTimeEntity> getBaseDao() {
        return this.dataOnlineTimeDao;
    }


    @Override
    public DataOnlineTimeDto saveDto(DataOnlineTimeDto dataOnlineTimeDto) {
        DataOnlineTimeEntity dataOnlineTimeEntity = dataOnlineTimeMapper.toEntity(dataOnlineTimeDto);
        dataOnlineTimeEntity = this.saveNotNull(dataOnlineTimeEntity);
        return this.dataOnlineTimeMapper.toDto(dataOnlineTimeEntity);
    }

    @Override
    public void deleteByDataClassId(String dataClassId) {
        this.dataOnlineTimeDao.deleteByDataClassId(dataClassId);
    }
}
