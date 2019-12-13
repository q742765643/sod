package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataClassDao;
import com.piesat.dm.entity.DataClassEntity;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.dm.rpc.mapper.DataClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月22日 16:31:15
 */
@Service
public class DataClassServiceImpl extends BaseService<DataClassEntity> implements DataClassService {
    @Autowired
    private DataClassDao dataClassDao;
    @Autowired
    private DataClassMapper dataClassMapper;

    @Override
    public BaseDao<DataClassEntity> getBaseDao() {
        return dataClassDao;
    }

    @Override
    public DataClassDto saveDto(DataClassDto dataClassDto) {
        DataClassEntity dataClassEntity = this.dataClassMapper.toEntity(dataClassDto);
        dataClassEntity = this.save(dataClassEntity);
        return this.dataClassMapper.toDto(dataClassEntity);
    }

    @Override
    public List<DataClassDto> all() {
        List<DataClassEntity> all = this.getAll();
        return this.dataClassMapper.toDto(all);
    }

    @Override
    public DataClassDto getDotById(String id) {
        DataClassEntity dataClassEntity = this.getById(id);
        return this.dataClassMapper.toDto(dataClassEntity);
    }
}
