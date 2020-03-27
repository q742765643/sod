package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DatabaseSpecialEntity;
import com.piesat.dm.entity.DatabaseUserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专题库管理
 */
@Repository
public interface DatabaseSpecialDao extends BaseDao<DatabaseSpecialEntity> {

    List<DatabaseSpecialEntity>  findByExamineStatusAndOrderBySortNoAscCreateTimeDesc(String examineStatus);

}
