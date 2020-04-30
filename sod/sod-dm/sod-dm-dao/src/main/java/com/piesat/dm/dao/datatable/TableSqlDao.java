package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.TableSqlEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * 表sql
 *
 * @author cwh
 * @date 2020年 04月02日 14:05:09
 */
@Repository
public interface TableSqlDao extends BaseDao<TableSqlEntity> {

    @Transactional
    void deleteByDatabaseIdAndTableName(String databaseId,String tableName);
}
