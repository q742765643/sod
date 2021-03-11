package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.TablePartEntity;
import org.springframework.stereotype.Repository;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月16日 15:49:15
 */
@Repository
public interface ShardingDao extends BaseDao<TablePartEntity> {
}
