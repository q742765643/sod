package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.TableForeignKeyEntity;

import java.util.List;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:08:41
 */
public interface TableForeignKeyDao extends BaseDao<TableForeignKeyEntity> {
    List<TableForeignKeyEntity> findByClassLogicId(String classLogicId);
    int deleteByIdIn(List<String> ids);
}
