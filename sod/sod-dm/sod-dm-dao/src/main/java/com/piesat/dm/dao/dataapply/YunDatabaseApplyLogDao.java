package com.piesat.dm.dao.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyLogEntity;

import java.util.List;


public interface YunDatabaseApplyLogDao extends BaseDao<YunDatabaseApplyLogEntity> {
    List<YunDatabaseApplyLogEntity> findByLogId(String logId);

    void deleteByLogId(String logId);
}
