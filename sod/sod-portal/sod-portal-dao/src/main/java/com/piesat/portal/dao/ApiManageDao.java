package com.piesat.portal.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.portal.entity.ApiManageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiManageDao extends BaseDao<ApiManageEntity> {

    List<ApiManageEntity> findByApiSys(String apiSys);

    List<ApiManageEntity> findByApiSysAndApiName(String apiSys,String apiName);

}
