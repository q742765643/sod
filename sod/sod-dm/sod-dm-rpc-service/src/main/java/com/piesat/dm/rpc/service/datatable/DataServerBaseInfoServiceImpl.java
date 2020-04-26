package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.datatable.DataServerBaseInfoDao;
import com.piesat.dm.entity.datatable.DataServerBaseInfoEntity;
import com.piesat.dm.rpc.api.datatable.DataServerBaseInfoService;
import com.piesat.dm.rpc.dto.datatable.DataServerBaseInfoDto;
import com.piesat.dm.rpc.mapper.datatable.DataServerBaseInfoMapper;
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
        dataServerBaseInfoEntity = this.saveNotNull(dataServerBaseInfoEntity);
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

    @Override
    public List<DataServerBaseInfoDto> findByDataCLassId(String dataCLassId) {
        List<DataServerBaseInfoEntity> all = this.dataServerBaseInfoDao.findByDataCLassId(dataCLassId);
        return this.dataServerBaseInfoMapper.toDto(all);
    }
}
