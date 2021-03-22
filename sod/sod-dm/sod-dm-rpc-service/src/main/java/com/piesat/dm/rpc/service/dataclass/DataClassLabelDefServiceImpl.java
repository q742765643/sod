package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.dataclass.DataClassLabelDefDao;
import com.piesat.dm.entity.dataclass.DataClassLabelDefEntity;
import com.piesat.dm.rpc.api.dataclass.DataClassLabelDefService;
import com.piesat.dm.rpc.dto.dataclass.DataClassLabelDefDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassLabelDefMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据标签定义
 *
 * @author cwh
 * @date 2020年 07月31日 16:48:06
 */
@Service
public class DataClassLabelDefServiceImpl extends BaseService<DataClassLabelDefEntity> implements DataClassLabelDefService {
    @Autowired
    private DataClassLabelDefDao dataClassLabelDefDao;
    @Autowired
    private DataClassLabelDefMapper dataClassLabelDefMapper;

    @Override
    public BaseDao<DataClassLabelDefEntity> getBaseDao() {
        return this.dataClassLabelDefDao;
    }

    @Override
    public DataClassLabelDefDto saveDto(DataClassLabelDefDto dataClassLabelDefDto) {
        DataClassLabelDefEntity dataClassLabelDefEntity = this.dataClassLabelDefMapper.toEntity(dataClassLabelDefDto);
        DataClassLabelDefEntity save = this.save(dataClassLabelDefEntity);
        return this.dataClassLabelDefMapper.toDto(save);
    }

    @Override
    public DataClassLabelDefDto getDotById(String id) {
        DataClassLabelDefEntity byId = this.getById(id);
        return this.dataClassLabelDefMapper.toDto(byId);
    }

    @Override
    public List<DataClassLabelDefDto> all() {
        List<DataClassLabelDefEntity> all = this.getAll();
        return this.dataClassLabelDefMapper.toDto(all);
    }

}
