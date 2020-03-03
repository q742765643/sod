package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.TableIndexEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableIndexDao extends BaseDao<TableIndexEntity> {
    List<TableIndexEntity> findByTableId(String tableId);
    int deleteByTableId(String tableId);
}
