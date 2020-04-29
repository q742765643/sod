package com.piesat.dm.dao.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataapply.DataAuthorityApplyEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 12:56
 */
@Repository
public interface DataAuthorityApplyDao extends BaseDao<DataAuthorityApplyEntity> {
    List<DataAuthorityApplyEntity> findByUserId(String userId);

}
