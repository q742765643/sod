package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.DataTableInfoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataTableDao extends BaseDao<DataTableInfoEntity> {

    @Query(value = "select a from DataTableInfoEntity a,DataClassAndTableEntity b " +
            "where a.id=b.tableId and a.databaseId =?1 and (?2 is null or b.dataClassId = ?2) ")
    List<DataTableInfoEntity> getByDatabaseIdAndClassId(String databaseId, String dataClassId);

    /**
     * 根据数据库id查询
     *
     * @param databaseId
     * @return
     */
    List<DataTableInfoEntity> findByDatabaseId(String databaseId);

    /**
     * 根据关联id查询
     *
     * @param classLogic
     * @return
     */
    @Query(value = "select a from DataTableInfoEntity a,DataClassAndTableEntity b " +
            "where (a.id=b.tableId or a.id=b.subTableId) and b.id =?1 ")
    List<DataTableInfoEntity> getByClassLogicId(String classLogic);

    /**
     * 根据存储编码和数据库id查询
     *
     * @param dataClassId
     * @param databaseId
     * @return
     */
    @Query(value = "select a from DataTableInfoEntity a,DataClassAndTableEntity b " +
            "where (a.id=b.tableId or a.id=b.subTableId) and b.dataClassId =?1 and a.databaseId = ?2  ")
    List<DataTableInfoEntity> getByClassIdAndDatabaseId(String dataClassId, String databaseId);

    /**
     * 根据表名查询
     *
     * @param tableName
     * @return
     */
    List<DataTableInfoEntity> findByTableName(String tableName);

    /**
     * 根据数据库id和表类型查询
     *
     * @param databaseId
     * @param tableType
     * @return
     */
    @Query(value = "SELECT a.* FROM T_SOD_DATA_TABLE_INFO a,T_SOD_DATACLASS_TABLE b " +
            "WHERE (a.id=b.TABLE_ID OR a.id=b.SUB_TABLE_ID) and a.database_id = ?1 and a.table_type =?2 ", nativeQuery = true)
    List<Map<String, Object>> findETables(String databaseId, String tableType);

    /**
     * 根据表id返回键表要素表
     *
     * @param tableId
     * @return
     */
    @Query(value =
            "SELECT DISTINCT a.*,c.DICT_LABEL from T_SOD_DATA_TABLE_INFO a " +
                    "LEFT JOIN T_SOD_DICT_DATA c ON a.STORAGE_TYPE = c.dict_value " +
                    "LEFT JOIN T_SOD_DATA_TABLE_FOREIGNKEY b ON a.id=b.TABLE_ID OR a.id=b.SUB_TABLE_ID " +
                    "WHERE b.TABLE_ID=?1 OR b.SUB_TABLE_ID=?1 OR a.id=?1 ", nativeQuery = true)
    List<Map<String, Object>> getTables(String tableId);

    /**
     * 根据表id关联表
     *
     * @param tableId
     * @return
     */
    @Query(value =
            "SELECT DISTINCT a.*,c.DICT_LABEL,d.DATABASE_NAME,e.DATABASE_NAME DATABASE_NAME_P FROM T_SOD_DATA_TABLE_INFO a " +
                    "LEFT JOIN T_SOD_DICT_DATA c ON a.STORAGE_TYPE = c.dict_value " +
                    "LEFT JOIN T_SOD_DATACLASS_TABLE b ON a.id=b.TABLE_ID OR a.id=b.SUB_TABLE_ID " +
                    "LEFT JOIN T_SOD_DATABASE d ON a.database_id = d.id " +
                    "LEFT JOIN T_SOD_DATABASE_DEFINE e ON d.DATABASE_DEFINE_ID = e.id " +
                    "WHERE b.TABLE_ID=?1 OR b.SUB_TABLE_ID=?1 ", nativeQuery = true)
    List<Map<String, Object>> getRelatedTables(String tableId);

    /**
     * 根据子表类型查询
     * @param tableType
     * @param storageType
     * @return
     */
    @Query(value = "SELECT * FROM T_SOD_DATA_TABLE_INFO WHERE TABLE_TYPE = ?1 AND STORAGE_TYPE = ?2 ", nativeQuery = true)
    List<Map<String, Object>> findBySubType(String tableType,String storageType);
}
