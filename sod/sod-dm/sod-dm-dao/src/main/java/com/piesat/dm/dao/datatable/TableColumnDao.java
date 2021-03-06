package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TableColumnDao extends BaseDao<TableColumnEntity> {
    List<TableColumnEntity> findByTableId(String tableId);
    @Transactional(rollbackFor = Exception.class)
    int deleteByIdIn(List<String> ids);
    @Transactional(rollbackFor = Exception.class)
    int deleteByTableId(String tableId);

    List<TableColumnEntity> findByTableIdAndIsPrimaryKeyTrue(String tableId);

    /**
     * 查询最大序号
     * @param tableId
     * @return
     */
    TableColumnEntity findFirstByTableIdOrderBySerialNumberDesc(String tableId);

}
