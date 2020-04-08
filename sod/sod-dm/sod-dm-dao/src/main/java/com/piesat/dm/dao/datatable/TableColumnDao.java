package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TableColumnDao extends BaseDao<TableColumnEntity> {
    List<TableColumnEntity> findByTableId(String tableId);

    int deleteByIdIn(List<String> ids);

    int deleteByTableId(String tableId);

    List<TableColumnEntity> findByTableIdAndIsPrimaryKeyTrue(String tableId);

}