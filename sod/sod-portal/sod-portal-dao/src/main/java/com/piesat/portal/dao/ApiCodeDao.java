package com.piesat.portal.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.portal.entity.ApiCodeEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApiCodeDao extends BaseDao<ApiCodeEntity> {

    List<ApiCodeEntity> findByApiId(String apiId);

    @Transactional(rollbackFor = Exception.class)
    void deleteByApiId(String apiId);

}
