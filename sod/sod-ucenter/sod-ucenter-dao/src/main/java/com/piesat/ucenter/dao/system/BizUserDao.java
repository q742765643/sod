package com.piesat.ucenter.dao.system;

import com.piesat.common.jpa.BaseDao;
import com.piesat.ucenter.entity.system.BizUserEntity;
import com.piesat.ucenter.entity.system.DeptEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 业务注册用户
 *
 * @author cwh
 * @date 2020年 04月17日 17:23:01
 */
@Deprecated
@Repository
public interface BizUserDao extends BaseDao<BizUserEntity> {
    BizUserEntity findByBizUserIdAndChecked(String userId,String check);
    BizUserEntity findByBizUserId(String id);
}
