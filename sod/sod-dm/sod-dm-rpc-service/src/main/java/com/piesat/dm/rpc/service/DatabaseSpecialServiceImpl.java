package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Cassandra;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.*;
import com.piesat.dm.entity.*;
import com.piesat.dm.rpc.api.DatabaseSpecialService;
import com.piesat.dm.rpc.api.DatabaseUserService;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.dm.rpc.dto.DatabaseSpecialAuthorityDto;
import com.piesat.dm.rpc.dto.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.DatabaseUserDto;
import com.piesat.dm.rpc.mapper.DatabaseSpecialMapper;
import com.piesat.dm.rpc.mapper.DatabaseUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 专题库管理
 */
@Service
public class DatabaseSpecialServiceImpl extends BaseService<DatabaseSpecialEntity> implements DatabaseSpecialService {
    @Autowired
    private DatabaseSpecialDao databaseSpecialDao;
    @Autowired
    private DatabaseSpecialMapper databaseSpecialMapper;
    @Autowired
    private DatabaseUserDao databaseUserDao;
    @Autowired
    private DatabaseSpecialAuthorityDao databaseSpecialAuthorityDao;

    @Override
    public BaseDao<DatabaseSpecialEntity> getBaseDao() {
        return databaseSpecialDao;
    }

    @Override
    public List<DatabaseSpecialDto> all() {
        List<DatabaseSpecialEntity> all = this.getAll();
        return this.databaseSpecialMapper.toDto(all);
    }

    @Override
    public DatabaseSpecialDto getDotById(String id) {
        DatabaseSpecialEntity databaseSpecialEntity = this.getById(id);
        return this.databaseSpecialMapper.toDto(databaseSpecialEntity);
    }

    @Override
    public DatabaseSpecialDto saveDto(DatabaseSpecialDto databaseSpecialDto) {
        DatabaseSpecialEntity databaseSpecialEntity = this.databaseSpecialMapper.toEntity(databaseSpecialDto);
        this.save(databaseSpecialEntity);
        return this.databaseSpecialMapper.toDto(databaseSpecialEntity);
    }

    /**
     * 数据库授权
     * @param databaseDto
     */
    @Override
    public void empowerDatabaseSperial(DatabaseDto databaseDto) {
        try {
            String userId = databaseDto.getUserId();
            //判断用户是否申请过数据库账户
            List<DatabaseUserEntity> databaseUserEntityList = databaseUserDao.findByUserId(userId);
            if(databaseUserEntityList==null || databaseUserEntityList.size()==0){
                return;
            }
            //删除历史授权记录，重新添加
            String sdbId = databaseDto.getTdbId();
            databaseSpecialAuthorityDao.deleteBySdbId(sdbId);
            //需要授权的数据库列表
            List<DatabaseSpecialAuthorityDto> databaseSpecialAuthorityList = databaseDto.getDatabaseSpecialAuthorityList();
            for(int i=0; i<databaseSpecialAuthorityList.size();i++){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
