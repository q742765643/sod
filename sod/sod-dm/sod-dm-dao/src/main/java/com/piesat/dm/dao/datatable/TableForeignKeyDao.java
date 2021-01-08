package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.TableForeignKeyEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:08:41
 */
@Repository
public interface TableForeignKeyDao extends BaseDao<TableForeignKeyEntity> {

    /**
     * 根据id查询
     * @param tableId
     * @return
     */
    List<TableForeignKeyEntity> findByTableId(String tableId);

    /**
     * 根据id查询相关表
     * @param tableId
     * @param subTableId
     * @return
     */
    List<TableForeignKeyEntity> findByTableIdOrSubTableId(String tableId,String subTableId);

    /**
     * 根据id删除
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    int deleteByIdIn(List<String> ids);

    /**
     * 查询外键信息
     * @param tableId
     * @return
     */
    @Query(value = "SELECT *," +
            "(SELECT TABLE_NAME FROM T_SOD_DATA_TABLE_INFO B WHERE A.TABLE_ID = B.ID) TABLE_NAME, " +
            "(SELECT TABLE_NAME FROM T_SOD_DATA_TABLE_INFO B WHERE A.SUB_TABLE_ID = B.ID) SUB_TABLE_NAME " +
            "FROM T_SOD_DATA_TABLE_FOREIGNKEY A WHERE TABLE_ID = ?1 OR SUB_TABLE_ID = ?1 ", nativeQuery = true)
    List<Map<String, Object>> findList(String tableId);
}
