package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.dataclass.DataClassLabelDao;
import com.piesat.dm.entity.dataclass.DataClassLabelEntity;
import com.piesat.dm.rpc.api.dataclass.DataClassLabelService;
import com.piesat.dm.rpc.dto.dataclass.DataClassLabelDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassLabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据标签
 *
 * @author cwh
 * @date 2020年 07月31日 16:48:06
 */
@Service
public class DataClassLabelServiceImpl extends BaseService<DataClassLabelEntity> implements DataClassLabelService {
    @Autowired
    private DataClassLabelDao dataClassLabelDao;
    @Autowired
    private DataClassLabelMapper dataClassLabelMapper;

    @Override
    public BaseDao<DataClassLabelEntity> getBaseDao() {
        return this.dataClassLabelDao;
    }

    @Override
    public DataClassLabelDto saveDto(DataClassLabelDto dataClassLabelDto) {
        DataClassLabelEntity dataClassLabelEntity = this.dataClassLabelMapper.toEntity(dataClassLabelDto);
        DataClassLabelEntity save = this.save(dataClassLabelEntity);
        return this.dataClassLabelMapper.toDto(save);
    }

    @Override
    public List<DataClassLabelDto> saveList(List<DataClassLabelDto> dataLogicList) {
        List<DataClassLabelEntity> dataClassLabelEntities = this.dataClassLabelMapper.toEntity(dataLogicList);
        List<DataClassLabelEntity> save = this.save(dataClassLabelEntities);
        return this.dataClassLabelMapper.toDto(save);
    }

    @Override
    public void deleteByDataClassId(String dataclassId) {
        this.dataClassLabelDao.deleteByDataClassId(dataclassId);
    }

    @Override
    public List<DataClassLabelDto> findByDataClassId(String dataclassId) {
        List<DataClassLabelEntity> byDataClassId = this.dataClassLabelDao.findByDataClassId(dataclassId);
        return this.dataClassLabelMapper.toDto(byDataClassId);
    }
}
