package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.TableColumnEntity;
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

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update T_SOD_DATA_TABLE_COLUMN p set p.db_ele_code =?1, p.type=?2  where p.table_id = ?3", nativeQuery = true)
    int updateDto(String db_ele_code, String type, String table_id);

    List<TableColumnEntity> findByTableIdAndIsPrimaryKeyTrue(String tableId);

}
