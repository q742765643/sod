package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.ShardingEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月16日 15:49:15
 */
@Repository
public interface ShardingDao extends BaseDao<ShardingEntity> {
    List<ShardingEntity> findByTableId(String tableId);
    @Transactional
    void deleteByTableId(String tableId);
}
