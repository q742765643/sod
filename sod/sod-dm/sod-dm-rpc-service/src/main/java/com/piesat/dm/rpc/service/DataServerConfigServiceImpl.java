package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataServerConfigDao;
import com.piesat.dm.entity.DataServerConfigEntity;
import com.piesat.dm.rpc.api.DataServerConfigService;
import com.piesat.dm.rpc.dto.DataServerConfigDto;
import com.piesat.dm.rpc.mapper.DataServerConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务信息配置
 *
 * @author cwh
 * @date 2020年 02月12日 15:57:27
 */
@Service
public class DataServerConfigServiceImpl extends BaseService<DataServerConfigEntity> implements DataServerConfigService {
    @Autowired
    private DataServerConfigDao dataServerConfigDao;
    @Autowired
    private DataServerConfigMapper dataServerConfigMapper;

    @Override
    public BaseDao<DataServerConfigEntity> getBaseDao() {
        return this.dataServerConfigDao;
    }

    @Override
    public DataServerConfigDto saveDto(DataServerConfigDto dataServerConfigDto) {
        DataServerConfigEntity dataServerConfigEntity = this.dataServerConfigMapper.toEntity(dataServerConfigDto);
        dataServerConfigEntity = this.save(dataServerConfigEntity);
        return this.dataServerConfigMapper.toDto(dataServerConfigEntity);
    }

    @Override
    public DataServerConfigDto getDotById(String id) {
        DataServerConfigEntity dataServerConfigEntity = this.getById(id);
        return this.dataServerConfigMapper.toDto(dataServerConfigEntity);
    }

    @Override
    public List<DataServerConfigDto> all() {
        List<DataServerConfigEntity> all = this.getAll();
        return this.dataServerConfigMapper.toDto(all);
    }
}
