package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.TableIndexEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TableExportDao extends BaseDao<TableIndexEntity> {

    /**
     * 获取导出资料
     * @param dataClassIdParam
     * @param physicalDBParam
     * @return
     */
    @Query(value = "select * from t_sod_data_table where data_service_id in(?1)",nativeQuery = true)
    List<Map<String, Object>> getDataList(String dataClassIdParam, String physicalDBParam);

    /**
     * 获取列信息
     * @param tableId
     * @return
     */
    @Query(value = "select * from t_sod_data_table_column where table_id = ?1 order by SERIAL_NUMBER asc",nativeQuery = true)
    List<Map<String, Object>> getColumnList(String tableId);

    /**
     * 获取索引信息
     * @param tableId
     * @return
     */
    @Query(value = "select * from t_sod_data_table_index where table_id = ?1 ",nativeQuery = true)
    List<Map<String, Object>> getIndexList(String tableId);

    /**
     * 获取分区信息
     * @param tableId
     * @return
     */
    @Query(value = "select * from t_sod_data_table_sharding where table_id = ?1 ",nativeQuery = true)
    List<Map<String, Object>> getSharedList(String tableId,String physicalDBParam);

    /**
     * 获取导出资料类别
     * @param dataClassIdParam
     * @return
     */
    @Query(value = "select * from t_sod_data_class where data_class_id in(select distinct leftb(data_class_id,1) from t_sod_data_class where data_class_id in(?1))",nativeQuery = true)
    List<Map<String, Object>> getTypeList(String dataClassIdParam);
}
