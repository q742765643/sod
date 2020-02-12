package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataServerBaseInfoDao;
import com.piesat.dm.entity.DataServerBaseInfoEntity;
import com.piesat.dm.rpc.api.DataServerBaseInfoService;
import com.piesat.dm.rpc.dto.DataServerBaseInfoDto;
import com.piesat.dm.rpc.mapper.DataServerBaseInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务基础信息
 *
 * @author cwh
 * @date 2020年 02月12日 15:14:56
 */
@Service
public class DataServerBaseInfoServiceImpl extends BaseService<DataServerBaseInfoEntity> implements DataServerBaseInfoService {

    @Autowired
    private DataServerBaseInfoDao dataServerBaseInfoDao;
    @Autowired
    private DataServerBaseInfoMapper dataServerBaseInfoMapper;

    @Override
    public BaseDao<DataServerBaseInfoEntity> getBaseDao() {
        return this.dataServerBaseInfoDao;
    }

    @Override
    public DataServerBaseInfoDto saveDto(DataServerBaseInfoDto dataServerBaseInfoDto) {
        DataServerBaseInfoEntity dataServerBaseInfoEntity = this.dataServerBaseInfoMapper.toEntity(dataServerBaseInfoDto);
        dataServerBaseInfoEntity = this.save(dataServerBaseInfoEntity);
        return this.dataServerBaseInfoMapper.toDto(dataServerBaseInfoEntity);
    }

    @Override
    public DataServerBaseInfoDto getDotById(String id) {
        DataServerBaseInfoEntity dataServerBaseInfoEntity = this.getById(id);
        return this.dataServerBaseInfoMapper.toDto(dataServerBaseInfoEntity);
    }

    @Override
    public List<DataServerBaseInfoDto> all() {
        List<DataServerBaseInfoEntity> all = this.getAll();
        return this.dataServerBaseInfoMapper.toDto(all);
    }
}
