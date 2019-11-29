package com.piesat.ucenter.rpc.service.system;

import com.github.pagehelper.PageHelper;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.ucenter.mapper.system.UserMapper;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.ucenter.rpc.mapstruct.system.UserMapstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 17:13
 */
@Service
public class UserServiceImpl extends BaseService<UserEntity> implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserMapstruct userMapstruct;
    @Override
    public BaseDao<UserEntity> getBaseDao() {
        return userDao;
    }
    @Override
    public UserDto saveUserDto(UserDto userDto){
        UserEntity userEntity= userMapstruct.toEntity(userDto);
        userEntity=this.save(userEntity);
        userEntity= userMapper.selectByPrimaryKey("1");
        PageHelper.startPage(1,10);
        userMapper.selectByPrimaryKey("1");
        return userMapstruct.toDto(userEntity);

    }
    /**
     *@描述 根据用户名查找用户
     *@参数 [userName]
     *@返回值 com.piesat.ucenter.rpc.dto.system.UserDto
     *@author zzj
     *@创建时间 2019/11/28 16:38 
     **/
    @Override
    public UserDto selectUserByUserName(String userName){
        UserEntity userEntity=userDao.findByUserName(userName);
        return userMapstruct.toDto(userEntity);
    }
}
