package com.piesat.portal.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.portal.entity.FeedbackManageEntity;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackManageDao extends BaseDao<FeedbackManageEntity> {

    @Query(value="update T_SOD_PORTAL_FEEDBACK_MANAGE set update_by=?2, reply=?3 where id = ?1", nativeQuery=true)
    @Modifying
    void update(String id, String updateBy, String reply);

}
