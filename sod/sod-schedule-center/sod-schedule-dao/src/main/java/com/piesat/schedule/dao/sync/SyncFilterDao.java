package com.piesat.schedule.dao.sync;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.sync.SyncFilterEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/17 17:58
 */
@Repository
public interface SyncFilterDao extends BaseDao<SyncFilterEntity> {

    public void deleteById(Integer id);
    Boolean existsById(Integer id);
    public SyncFilterEntity findById(Integer id);
}
