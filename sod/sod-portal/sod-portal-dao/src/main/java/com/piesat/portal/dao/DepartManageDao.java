package com.piesat.portal.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.portal.entity.DepartManageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartManageDao extends BaseDao<DepartManageEntity> {
    List<DepartManageEntity> findByDeptunicode(String deptunicode);
    List<DepartManageEntity> findByDeptcode(String deptcode);
}
