package com.piesat.dm.dao.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataapply.CloudDatabaseApplyEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/12 16:11
 */
@Repository
public interface CloudDatabaseApplyDao extends BaseDao<CloudDatabaseApplyEntity> {
    List<CloudDatabaseApplyEntity> findByUserIdOrderByExamineStatusAscCreateTimeDesc(String userId);
}
