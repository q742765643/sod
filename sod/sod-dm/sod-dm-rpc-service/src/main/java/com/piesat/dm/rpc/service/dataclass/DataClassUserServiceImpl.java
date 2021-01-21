package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.dataclass.DataClassUserDao;
import com.piesat.dm.entity.dataclass.DataClassUserEntity;
import com.piesat.dm.rpc.api.dataclass.DataClassUserService;
import com.piesat.dm.rpc.dto.dataclass.DataClassUserDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资料和用户关系
 *
 * @author cwh
 * @date 2020年 07月31日 16:48:06
 */
@Service
public class DataClassUserServiceImpl extends BaseService<DataClassUserEntity> implements DataClassUserService {
    @Autowired
    private DataClassUserDao dataClassUserDao;
    @Autowired
    private DataClassUserMapper dataClassUserMapper;

    @Override
    public BaseDao<DataClassUserEntity> getBaseDao() {
        return this.dataClassUserDao;
    }

    @Override
    public DataClassUserDto saveDto(DataClassUserDto DataClassUserDto) {
        DataClassUserEntity DataClassUserEntity = this.dataClassUserMapper.toEntity(DataClassUserDto);
        DataClassUserEntity save = this.save(DataClassUserEntity);
        return this.dataClassUserMapper.toDto(save);
    }

    @Override
    public List<DataClassUserDto> saveList(List<DataClassUserDto> dataLogicList) {
        List<DataClassUserEntity> DataClassUserEntities = this.dataClassUserMapper.toEntity(dataLogicList);
        List<DataClassUserEntity> save = this.save(DataClassUserEntities);
        return this.dataClassUserMapper.toDto(save);
    }

    @Override
    public void deleteByDataClassId(String dataclassId) {
        this.dataClassUserDao.deleteByDataClassId(dataclassId);
    }

    @Override
    public List<DataClassUserDto> findByDataClassId(String dataclassId) {
        List<DataClassUserEntity> byDataClassId = this.dataClassUserDao.findByDataClassId(dataclassId);
        return this.dataClassUserMapper.toDto(byDataClassId);
    }

    @Override
    public List<DataClassUserDto> findByUserName(String userName) {
        List<DataClassUserEntity> byUserName = this.dataClassUserDao.findByUserName(userName);
        return this.dataClassUserMapper.toDto(byUserName);
    }
}
