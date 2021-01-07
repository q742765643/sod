package com.piesat.dm.dao.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyEntity;

import java.util.List;


public interface YunDatabaseApplyDao extends BaseDao<YunDatabaseApplyEntity> {
    List<YunDatabaseApplyEntity> findByUserIdOrderByExamineStatusAscCreateTimeDesc(String UserId);
    List<YunDatabaseApplyEntity> findByUserIdAndStorageLogic(String UserId,String StorageLogic);
    List<YunDatabaseApplyEntity> findByUserIdAndStorageLogicAndExamineStatusLikeAndDisplaynameLike(String UserId,String StorageLogic,String examineStatus,String displayname);
}
