package com.piesat.schedule.dao.sync;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.sync.SyncConfigEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/17 17:57
 */
@Repository
public interface SyncConfigDao extends BaseDao<SyncConfigEntity> {
    public void deleteById(Integer id);
    public Boolean existsById(Integer id);

    public SyncConfigEntity findById(Integer id);

    List<SyncConfigEntity> findByTargetTableId(String targetTableId);
}
