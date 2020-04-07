package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.GridAreaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:22:26
 */
@Repository
public interface GridAreaDao extends BaseDao<GridAreaEntity> {
    List<GridAreaEntity> findByDataServiceId(String dataServiceId);
}
