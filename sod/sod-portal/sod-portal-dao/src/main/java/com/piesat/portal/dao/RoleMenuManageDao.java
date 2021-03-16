package com.piesat.portal.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.portal.entity.RoleMenuManageEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMenuManageDao extends BaseDao<RoleMenuManageEntity> {
    void deleteByRoleId(String id);
}
