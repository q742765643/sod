package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.dataclass.DataClassNormDao;
import com.piesat.dm.entity.dataclass.DataClassNormEntity;
import com.piesat.dm.rpc.api.dataclass.DataClassNormService;
import com.piesat.dm.rpc.dto.dataclass.DataClassNormDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassNormMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 编码配置
 *
 * @author cwh
 * @date 2020年 02月12日 14:47:50
 */
@Service
public class DataClassNormServiceImpl extends BaseService<DataClassNormEntity> implements DataClassNormService {

    @Autowired
    private DataClassNormDao dataClassNormDao;
    @Autowired
    private DataClassNormMapper dataClassNormMapper;


    @Override
    public BaseDao<DataClassNormEntity> getBaseDao() {
        return this.dataClassNormDao;
    }

    @Override
    public DataClassNormDto saveDto(DataClassNormDto dataClassNormDto) {
        DataClassNormEntity dataClassNormEntity = this.dataClassNormMapper.toEntity(dataClassNormDto);
        dataClassNormEntity = this.saveNotNull(dataClassNormEntity);
        return this.dataClassNormMapper.toDto(dataClassNormEntity);
    }

    @Override
    public DataClassNormDto getDotById(String id) {
        DataClassNormEntity dataClassNormEntity = this.getById(id);
        return this.dataClassNormMapper.toDto(dataClassNormEntity);
    }

    @Override
    public List<DataClassNormDto> all() {
        List<DataClassNormEntity> all = this.getAll();
        return this.dataClassNormMapper.toDto(all);
    }
}
