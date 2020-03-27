package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DataOnlineTimeEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/17 19:17
 */
@Repository
public interface DataOnlineTimeDao extends BaseDao<DataOnlineTimeEntity> {
    void deleteByDataClassId(String dataClassId);

    DataOnlineTimeEntity findByDataClassId(String dataClassId);
}
