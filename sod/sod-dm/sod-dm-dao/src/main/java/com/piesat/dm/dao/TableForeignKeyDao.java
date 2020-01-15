package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.TableForeignKeyEntity;

import java.util.List;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:08:41
 */
public interface TableForeignKeyDao extends BaseDao<TableForeignKeyEntity> {
    List<TableForeignKeyEntity> findByTableId(String tableId);
}
