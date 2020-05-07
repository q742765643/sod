package com.piesat.sod.system.dao;

import org.springframework.stereotype.Repository;

import com.piesat.common.jpa.BaseDao;
import com.piesat.sod.system.entity.ManageGroupEntity;

import java.util.List;

/** 管理字段分组实体
*@description
*@author wlg
*@date 2020年1月16日下午5:10:03
*
*/
@Repository
public interface ManageGroupDao extends BaseDao<ManageGroupEntity>{
    ManageGroupEntity findByGroupName(String groupName);
}
