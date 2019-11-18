package com.piesat.ucenter.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.ucenter.entity.UserEntity;
import com.piesat.ucenter.rpc.api.UserService;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 11:45
 */
public class UserServiceImpl extends BaseService<UserEntity> implements UserService {
    @Override
    public BaseDao<UserEntity> getBaseDao() {
        return null;
    }
}
