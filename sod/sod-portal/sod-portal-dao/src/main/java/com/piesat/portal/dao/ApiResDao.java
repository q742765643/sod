package com.piesat.portal.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.portal.entity.ApiResEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApiResDao extends BaseDao<ApiResEntity> {

    List<ApiResEntity> findByApiId(String apiId);

    @Transactional
    void deleteByApiId(String apiId);
}
