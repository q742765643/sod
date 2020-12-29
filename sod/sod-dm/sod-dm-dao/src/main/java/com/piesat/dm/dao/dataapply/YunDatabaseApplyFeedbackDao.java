package com.piesat.dm.dao.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyFeedbackEntity;

import java.util.List;


public interface YunDatabaseApplyFeedbackDao extends BaseDao<YunDatabaseApplyFeedbackEntity> {
    List<YunDatabaseApplyFeedbackEntity> findByItserviceIdAndFeedbackStatusLike(String itserviceId,String feedbackStatus);

}
