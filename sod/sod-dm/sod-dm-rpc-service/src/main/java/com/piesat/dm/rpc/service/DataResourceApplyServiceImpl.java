package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataResourceApplyDao;
import com.piesat.dm.entity.DataResourceApplyEntity;
import com.piesat.dm.rpc.api.DataResourceApplyService;
import com.piesat.dm.rpc.dto.DataResourceApplyDto;
import com.piesat.dm.rpc.mapper.DataResourceApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/30 13:10
 */
@Service
public class DataResourceApplyServiceImpl extends BaseService<DataResourceApplyEntity> implements DataResourceApplyService {

    @Autowired
    private DataResourceApplyDao dataResourceApplyDao;

    @Autowired
    private DataResourceApplyMapper dataResourceApplyMapper;

    @Override
    public BaseDao<DataResourceApplyEntity> getBaseDao() {
        return dataResourceApplyDao;
    }

    @Override
    public DataResourceApplyDto saveDto(DataResourceApplyDto dataResourceApplyDto) {
        DataResourceApplyEntity dataResourceApplyEntity = dataResourceApplyMapper.toEntity(dataResourceApplyDto);
        dataResourceApplyEntity = this.saveNotNull(dataResourceApplyEntity);
        return dataResourceApplyMapper.toDto(dataResourceApplyEntity);
    }
}
