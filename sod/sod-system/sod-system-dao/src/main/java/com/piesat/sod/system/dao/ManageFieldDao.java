package com.piesat.sod.system.dao;

import org.hibernate.validator.constraints.EAN;
import org.springframework.stereotype.Repository;

import com.piesat.common.jpa.BaseDao;
import com.piesat.sod.system.entity.ManageFieldEntity;

import java.util.List;

/** 管理字段
*@description
*@author wlg
*@date 2020年1月16日下午5:23:49
*
*/
@Repository
public interface ManageFieldDao extends BaseDao<ManageFieldEntity>{
}
