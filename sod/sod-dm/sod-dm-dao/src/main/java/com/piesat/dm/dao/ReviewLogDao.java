package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.ReviewLogEntity;
import org.springframework.stereotype.Repository;

/**
 * @author cwh
 * @program: sod
 * @description: 审核日志
 * @date 2021年 01月18日 18:36:17
 */
@Repository
public interface ReviewLogDao extends BaseDao<ReviewLogEntity> {
}
