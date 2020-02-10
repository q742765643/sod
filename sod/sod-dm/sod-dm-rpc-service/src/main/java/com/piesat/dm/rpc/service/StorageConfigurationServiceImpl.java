package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.StorageConfigurationDao;
import com.piesat.dm.entity.StorageConfigurationEntity;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.mapper.StorageConfigurationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:28
 */
@Service
public class StorageConfigurationServiceImpl extends BaseService<StorageConfigurationEntity> implements StorageConfigurationService {
    @Autowired
    private StorageConfigurationDao storageConfigurationDao;
    @Autowired
    private StorageConfigurationMapper storageConfigurationMapper;
    @Override
    public BaseDao<StorageConfigurationEntity> getBaseDao() {
        return storageConfigurationDao;
    }

    @Override
    public StorageConfigurationDto saveDto(StorageConfigurationDto storageConfigurationDto) {
        StorageConfigurationEntity storageConfigurationEntity = this.storageConfigurationMapper.toEntity(storageConfigurationDto);
        storageConfigurationEntity = this.storageConfigurationDao.save(storageConfigurationEntity);
        return this.storageConfigurationMapper.toDto(storageConfigurationEntity);
    }
}
