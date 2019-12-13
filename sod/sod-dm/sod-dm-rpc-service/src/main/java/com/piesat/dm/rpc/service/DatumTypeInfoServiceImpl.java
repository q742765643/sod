package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatumTypeInfoDao;
import com.piesat.dm.entity.DatumTypeInfoEntity;
import com.piesat.dm.rpc.api.DatumTypeInfoService;
import com.piesat.dm.rpc.dto.DatumTypeInfoDto;
import com.piesat.dm.rpc.mapper.DatumTypeInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月29日 10:03:19
 */
@Service
public class DatumTypeInfoServiceImpl extends BaseService<DatumTypeInfoEntity> implements DatumTypeInfoService {
    @Autowired
    private DatumTypeInfoDao datumTypeInfoDao;
    @Autowired
    private DatumTypeInfoMapper datumTypeInfoMapper;


    @Override
    public BaseDao<DatumTypeInfoEntity> getBaseDao() {
        return this.datumTypeInfoDao;
    }

    @Override
    public DatumTypeInfoDto saveDto(DatumTypeInfoDto datumTypeInfoDto) {
        DatumTypeInfoEntity datumTypeInfoEntity = this.datumTypeInfoMapper.toEntity(datumTypeInfoDto);
        datumTypeInfoEntity = this.save(datumTypeInfoEntity);
        return this.datumTypeInfoMapper.toDto(datumTypeInfoEntity);
    }

    @Override
    public List<DatumTypeInfoDto> all() {
        List<DatumTypeInfoEntity> all = this.getAll();
        return this.datumTypeInfoMapper.toDto(all);
    }

    @Override
    public DatumTypeInfoDto getDotById(String id) {
        DatumTypeInfoEntity datumTypeInfoEntity = this.getById(id);
        return this.datumTypeInfoMapper.toDto(datumTypeInfoEntity);
    }
}
