package com.piesat.portal.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.portal.entity.UserRoleManageEntity;

public interface UserRoleManageDao extends BaseDao<UserRoleManageEntity> {
    void deleteByUserId(String id);
}
