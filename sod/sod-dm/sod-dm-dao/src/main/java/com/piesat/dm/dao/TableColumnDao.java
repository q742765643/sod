package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.TableColumnEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableColumnDao extends BaseDao<TableColumnEntity> {
    List<TableColumnEntity> findByTableId(String tableId);
}
