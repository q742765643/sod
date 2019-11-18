package com.piesat.ucenter.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.ucenter.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 11:03
 */
@Repository
public interface UserDao extends BaseDao<UserEntity> {
}
