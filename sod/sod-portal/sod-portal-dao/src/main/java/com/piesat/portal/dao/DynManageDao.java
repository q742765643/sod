package com.piesat.portal.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.portal.entity.DynManageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynManageDao extends BaseDao<DynManageEntity> {

    List<DynManageEntity> findByIspublished(String ispublished);
}
