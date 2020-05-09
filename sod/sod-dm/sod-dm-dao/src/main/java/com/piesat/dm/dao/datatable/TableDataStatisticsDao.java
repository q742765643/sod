package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.TableDataStatisticsEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 表数据统计
 *
 * @author cwh
 * @date 2020年 02月13日 14:49:04
 */
@Repository
public interface TableDataStatisticsDao extends BaseDao<TableDataStatisticsEntity> {
    List<TableDataStatisticsEntity> findByDatabaseIdAndTableIdAndStatisticDate(String databaseId, String tableId, Date statisticDate);
}
