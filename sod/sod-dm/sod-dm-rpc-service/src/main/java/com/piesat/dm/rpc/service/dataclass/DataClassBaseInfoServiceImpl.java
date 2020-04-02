package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.dataclass.DataClassBaseInfoDao;
import com.piesat.dm.entity.dataclass.DataClassBaseInfoEntity;
import com.piesat.dm.rpc.api.dataclass.DataClassBaseInfoService;
import com.piesat.dm.rpc.dto.dataclass.DataClassBaseInfoDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassBaseInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资料基础信息
 *
 * @author cwh
 * @date 2020年 04月02日 09:11:19
 */
@Service
public class DataClassBaseInfoServiceImpl extends BaseService<DataClassBaseInfoEntity> implements DataClassBaseInfoService {
    @Autowired
    private DataClassBaseInfoDao dataClassBaseInfoDao;
    @Autowired
    private DataClassBaseInfoMapper dataClassBaseInfoMapper;

    @Override
    public BaseDao<DataClassBaseInfoEntity> getBaseDao() {
        return this.dataClassBaseInfoDao;
    }

    @Override
    public DataClassBaseInfoDto saveDto(DataClassBaseInfoDto dataClassBaseInfoDto) {
        DataClassBaseInfoEntity dataClassBaseInfoEntity = this.dataClassBaseInfoMapper.toEntity(dataClassBaseInfoDto);
        dataClassBaseInfoEntity = this.save(dataClassBaseInfoEntity);
        return this.dataClassBaseInfoMapper.toDto(dataClassBaseInfoEntity);
    }

    @Override
    public DataClassBaseInfoDto getDotById(String id) {
        DataClassBaseInfoEntity dataClassBaseInfoEntity = this.getById(id);
        return this.dataClassBaseInfoMapper.toDto(dataClassBaseInfoEntity);
    }

    @Override
    public List<DataClassBaseInfoDto> all() {
        List<DataClassBaseInfoEntity> all = this.getAll();
        return this.dataClassBaseInfoMapper.toDto(all);
    }
}
