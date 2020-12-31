package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataOnlineTimeEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/17 19:17
 */
@Repository
public interface DataOnlineTimeDao extends BaseDao<DataOnlineTimeEntity> {
    @Transactional(rollbackFor = Exception.class)
    void deleteByDataClassId(String dataClassId);

    DataOnlineTimeEntity findByDataClassId(String dataClassId);
}
