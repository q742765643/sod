package com.piesat.dm.dao.special;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.special.DatabaseSpecialEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专题库管理
 */
@Repository
public interface DatabaseSpecialDao extends BaseDao<DatabaseSpecialEntity> {

    List<DatabaseSpecialEntity>  findByExamineStatusOrderBySortNoAscCreateTimeDesc(String examineStatus);

    List<DatabaseSpecialEntity> findByUserIdAndUseStatus(String userId,String useStatus);

    List<DatabaseSpecialEntity> findByUseStatus(String useStatus);

    List<DatabaseSpecialEntity> findByUserId(String userId);

}
