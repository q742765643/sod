package com.piesat.ucenter.mapper.system;

import com.github.pagehelper.PageInfo;
import com.piesat.ucenter.entity.system.UserEntity;
import org.springframework.stereotype.Component;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 18:28
 */
@Component
public interface UserMapper {
    UserEntity selectByPrimaryKey(String id);
    PageInfo<UserEntity> findAll();

}
