package com.piesat.dm.rpc.service;

import com.alibaba.fastjson.JSONArray;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.common.codedom.CodeDOM;
import com.piesat.dm.common.tree.BaseParser;
import com.piesat.dm.common.tree.TreeLevel;
import com.piesat.dm.dao.*;
import com.piesat.dm.entity.*;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.api.DatabaseUserService;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DatabaseUserDto;
import com.piesat.dm.rpc.mapper.DataClassMapper;
import com.piesat.dm.rpc.mapper.DatabaseUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;

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
        DatabaseUserEntity databaseUserEntity = databaseUserDao.getByUpId(databaseUPId);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto getDotByUserId(String userId) {
        DatabaseUserEntity databaseUserEntity = databaseUserDao.getByUserId(userId);
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
