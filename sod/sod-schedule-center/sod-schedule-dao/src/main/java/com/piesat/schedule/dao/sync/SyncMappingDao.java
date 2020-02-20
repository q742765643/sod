package com.piesat.schedule.dao.sync;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.sync.SyncMappingEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/17 17:59
 */
@Repository
public interface SyncMappingDao extends BaseDao<SyncMappingEntity> {

    public List<SyncMappingEntity> findAllByTargetTableIdIn(List<String> targetTableId);
}
