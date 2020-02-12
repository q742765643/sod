package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DatabaseDao;
import com.piesat.dm.dao.DatabaseDefineDao;
import com.piesat.dm.dao.DatabaseUserDao;
import com.piesat.dm.entity.DatabaseAdministratorEntity;
import com.piesat.dm.entity.DatabaseDefineEntity;
import com.piesat.dm.entity.DatabaseEntity;
import com.piesat.dm.entity.DatabaseUserEntity;
import com.piesat.dm.rpc.api.DatabaseUserService;
import com.piesat.dm.rpc.dto.DatabaseUserDto;
import com.piesat.dm.rpc.mapper.DatabaseUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 数据库访问账户管理
 */
@Service
public class DatabaseUserServiceImpl extends BaseService<DatabaseUserEntity> implements DatabaseUserService {
    @Autowired
    private DatabaseUserDao databaseUserDao;
    @Autowired
    private DatabaseUserMapper databaseUserMapper;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DatabaseDefineDao databaseDefineDao;


    @Override
    public BaseDao<DatabaseUserEntity> getBaseDao() {
        return databaseUserDao;
    }

    @Override
    public List<DatabaseUserDto> all() {
        List<DatabaseUserEntity> all = this.getAll();
        return this.databaseUserMapper.toDto(all);
    }

    @Override
    public DatabaseUserDto getDotById(String id) {
        DatabaseUserEntity databaseUserEntity = this.getById(id);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto getDotByUPID(String databaseUPId) {
        DatabaseUserEntity databaseUserEntity = databaseUserDao.findByDatabaseUpId(databaseUPId);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto getDotByUserId(String userId) {
        DatabaseUserEntity databaseUserEntity = databaseUserDao.findByUserIdAndExamineStatusNot(userId,"2");
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto saveDto(DatabaseUserDto databaseUserDto) {
        DatabaseUserEntity databaseUserEntity = this.databaseUserMapper.toEntity(databaseUserDto);
        databaseUserEntity = this.save(databaseUserEntity);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public boolean empower(DatabaseUserDto databaseUserDto) {
        try{
            //根据ID获取旧的申请信息
            DatabaseUserEntity oldDatabaseUserEntity = this.getById(databaseUserDto.getId());

            /**为申请的IP授权**/
            //待授权Id
            String[] needEmpowerIdArr = databaseUserDto.getDatabaseUpId().split(",");
            List<String> needEmpowerIdist = Arrays.asList(needEmpowerIdArr);
            //非首次审核通过，授权的id中去掉以前的id
            if(oldDatabaseUserEntity.getExamineStatus().equals("1")){
                String[] haveEmpowerIdArr = oldDatabaseUserEntity.getDatabaseUpId().split(",");
                needEmpowerIdist.removeAll(Arrays.asList(haveEmpowerIdArr));
            }
            //待授权IP
            String[] needEmpowerIpArr = databaseUserDto.getDatabaseUpIp().split(";");
            for(String databaseId : needEmpowerIdist){
                DatabaseEntity databaseEntity = databaseDao.findById(databaseId).get();
                DatabaseDefineEntity databaseDefineEntity = databaseEntity.getDatabaseDefine();
                Set<DatabaseAdministratorEntity> databaseAdministratorSet = databaseDefineEntity.getDatabaseAdministratorList();
                //获取任意登录账号

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
