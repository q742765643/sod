package com.piesat.ucenter.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseMapper;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.ucenter.dao.UserDao;
import com.piesat.ucenter.entity.UserEntity;
import com.piesat.ucenter.rpc.api.UserService;
import com.piesat.ucenter.rpc.dto.UserDto;
import com.piesat.ucenter.rpc.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 11:45
 */
@Service
public class UserServiceImpl extends BaseService<UserEntity> implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMapper userMapper;
    @Override
    public BaseDao<UserEntity> getBaseDao() {
        return userDao;
    }
    @Override
    public UserDto save(UserDto userDto){
        UserEntity userEntity= userMapper.toEntity(userDto);
        userEntity=this.save(userEntity);
        return userMapper.toDto(userEntity);
    }
}
