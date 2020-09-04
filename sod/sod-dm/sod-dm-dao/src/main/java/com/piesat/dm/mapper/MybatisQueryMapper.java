package com.piesat.dm.mapper;

import com.piesat.dm.entity.dataapply.NewdataApplyEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.dataclass.DataClassBaseInfoEntity;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import com.piesat.dm.entity.dataclass.DataLogicEntity;
import com.piesat.dm.entity.datatable.*;
import com.piesat.dm.entity.special.DatabaseSpecialAccessEntity;
import com.piesat.dm.entity.special.DatabaseSpecialEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 联表查询Mapper
 *
 * @author cwh
 * @date 2020年 02月12日 09:26:56
 */
@Component
public interface MybatisQueryMapper {
    List<Map<String, Object>> getDataClassListBYIn(@Param("classIds") List<String> classIds, @Param("className") String className, @Param("dDataId") String dDataId);

    List<Map<String, Object>> getDataLogicByDatabaseId(String databaseId);

    List<Map<String, Object>> selectStorageConfigurationPageList(@Param("map") Map<String, String> map);

    List<Map<String, Object>> storageFieldList(@Param("map") Map<String, String> map);

    void updateStorageConfigurationStatus(@Param("id") String id, @Param("column") String column, @Param("value") String value);

    List<Map<String, Object>> findStorageConfigurationByDataClassId(@Param("dataClassId") String dataClassId);

    void deleteStorageConfigurationByDataClassId(@Param("dataClassId") String dataClassId);

    void updateNewdataApplyStatus(@Param("entity") NewdataApplyEntity newdataApplyEntity);

    List<Map<String, Object>> getByUserIdAndApplyId(@Param("entity") NewdataApplyEntity newdataApplyEntity);

    List<Map<String, Object>> getColumnByIdAndDDataId(@Param("entity") NewdataApplyEntity newdataApplyEntity);

    List<Map<String, Object>> getByDatabaseIdAndTableName(@Param("databaseId") String databaseId, @Param("tableName") String tableName);


    List<Map<String, Object>> selectNewdataApplyPageList(@Param("map") Map<String, Object> map);
    List<Map<String, Object>> selectDataApplyPageList(@Param("map") Map<String, Object> map);

    List<Map<String, Object>> queryNewdataApplyByApplyId(@Param("id") String id);

    List<Map<String, Object>> querySpecialByUserIdAndUseStatus(@Param("userId")String userId, @Param("useStatus")String useStatus);

    /**
     * 在线时间检索条件分页查询
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> onLineList(@Param("map") Map<String, String> map);

    List<Map<String, Object>> getLogicClassTree();

    List<Map<String, Object>> getLogicClassTreePostgresql();


    List<Map<String, Object>> getDatabaseTree();

    List<Map<String, Object>> getDatabaseTreePostgresql();

    List<Map<String, Object>> getDatabaseClassTree(@Param("id") String id);

    /**
     * 归档时间查询
     * @param ddataid
     * @return
     */
    List<Map<String, Object>> getArchive(@Param("ddataid") String ddataid);

    List<Map<String, Object>> getDatabaseClassTreeMysql(@Param("id") String id);

    List<Map<String, Object>> getDatabaseClassTreePostgresql(@Param("id") String id);

    List<Map<String, Object>> getDatabaseClassTreePMysql(@Param("classIds") List<String> classIds, @Param("id") String id);

    Map<String, Object> queryDataAuthorityApplyById(@Param("id") String id);

    List<Map<String, Object>> getRecordByApplyIdMysql(@Param("map") Map<String, String> map);

    List<Map<String, Object>> getRecordByApplyId(@Param("map") Map<String, String> map);

    void updateDataAuthorityRecord(@Param("id") String id, @Param("authorize") Integer authorize, @Param("cause") String cause);

    void updateDataAuthorityApply(@Param("id") String id, @Param("auditStatus") String auditStatus,@Param("examiner") String examiner,@Param("examineTime") Date examineTime);

    int getDataServiceMaxNum(@Param("data_service_id") String data_service_id);

    List<Map<String, Object>> getOnlineTime(@Param("classDataId") String classDataId, @Param("statisticDate") String statisticDate);

    List<Map<String, Object>> getTableInfoByClassId(@Param("dataClassId") String dataClassId);

    List<Map<String, Object>> getInfoByUserIdMysql(@Param("userId") String userId);

    List<Map<String, Object>> getInfoByUserId(@Param("userId") String userId);

    List<Map<String, Object>> querySpecialReadWriteBySdbId(@Param("sdbId") String sdbId);

    void updateSpecialTreeData(@Param("sdbId") String sdbId,@Param("parentId") String parentId,@Param("typeId") String typeId,@Param("typeName") String typeName);

    List<Map<String, Object>> getRecordSpecialByTdbId(@Param("sdbId") String sdbId, @Param("userId") String userId);

    List<Map<String, Object>> getAllOtherRecordByUserId(@Param("userId") String userId, @Param("useStatus") String useStatus);

    List<Map<String, Object>> getLogicByUserId(@Param("databaseIds") String[] databaseIds);

    List<Map<String, Object>> getRecordListByUserIdMysql(@Param("userId") String userId);

    List<Map<String, Object>> getRecordListByUserId(@Param("userId") String userId);

    DataClassBaseInfoEntity getDataClassBaseInfo(@Param("id") String id);

    List<CmccElementEntity> queryCmccElements(DatumTableEntity datumTableEntity);

    List<Map<String, Object>> getLogicByDdataId(@Param("dDataId") String dDataId);

    List<DataLogicEntity> getDataLogic(@Param("dataclassId") String dataclassId, @Param("databaseId") String databaseId, @Param("tableName") String tableName);

    List<Map<String, Object>> getDataTypeList(@Param("dataClassIds") String dataClassIds);

    List<Map<String, Object>> getDataTypeListPostgresql(@Param("dataClassIds") String dataClassIds);

    List<Map<String, Object>> findByDataServiceId(@Param("dataClassIds") String dataClassIds,@Param("databaseId") String databaseId);

    List<Map<String, Object>> findColumnByTableId(@Param("tableId") String tableId);

    List<Map<String, Object>> findIndexByTableId(@Param("tableId") String tableId);

    List<Map<String, Object>> findShardByTableId(@Param("tableId") String tableId);

    Map<String, Object> findByDatabaseDefine(@Param("databaseId") String databaseId);

    List<Map<String, Object>> findByUserIdAndDatabaseDefineId1(@Param("userId")String userId,@Param("databaseDefineId")String databaseDefineId);
    List<Map<String, Object>> findByUserIdAndDatabaseDefineId2(@Param("userId")String userId,@Param("databaseDefineId")String databaseDefineId);

    List<Map<String, Object>> getSqlList(@Param("databaseId") String databaseId, @Param("dataClassIds") String dataClassIds);
	/**
     * 查询专题库资料列表
     * @param map
     * @return
     */
    List<Map<String, Object>> getDatabaseSpecialReadWriteList(@Param("map")Map<String,Object> map);
    List<Map<String, Object>> queryTableBylogics(List<String> logics);

    List<Map<String, Object>> getGroupConcat(List<String> logics);

    List<Map<String, Object>> getDataAuthorityList(Map<String, Object> paraMap);

    void delDataAuthorityByApplyId(Map<String, Object> paraMap);

    void clearUselessApply();

    List<Map<String, Object>> getDataCategory();

    List<Map<String, String>> getSchemaByPhysic(Map<String, Object> param);

    List<Map<String, Object>> getRecordByTdbId(@Param("tdbId")String tdbId, @Param("typeId")String typeId, @Param("status")String status);

    List<Map<String, Object>> getAuthorizeRecordByTdbId(@Param("tdbId")String tdbId, @Param("typeId")String typeId,@Param("status") String status);

    void changeDataStatus(Map<String, String> dataMap);

    void changeDataAuthorStatus(Map<String, String> dataMap);

    void deleteRecordByTdbId(Map<String, String> dataMap);

    void deleteDataAuthor(Map<String, String> dataMap);

    void delTreeUpdateTypeId(Map<String, String> paraMap);

    void updateTypeIdByTdbId(Map<String, String> dataMap);

    List<DatabaseSpecialAccessEntity> getAllRecordByUserId(String userId);

    void saveOneRecord(DatabaseSpecialAccessEntity oneRecord);

    List<Map<String, Object>> getSpecialAuthorizeList(String user_id);

    void updateDataAuthorityStatus(String apply_id, String database_id, String data_class_id, Integer authorize);

    List<Map<String, Object>> getRecentOnlineTime(String ctsCode);
    /**
     *   获取用户可申请资料
     * @description
     * @author wlg
     * @date 2020年4月22日下午5:07:55
     * @param userId
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> getApplyDataInfo(String userId) throws Exception;

    List<LinkedHashMap<String, Object>> getDataClassInfo(String dataClassId);

    List<LinkedHashMap<String, Object>> selectTabOmincmccTempele(String c_coremeta_id);

    List<LinkedHashMap<String, Object>> selectGridAreaDefine(String dataClassId);

    List<LinkedHashMap<String, Object>> getDataclassTreeByClassIds(List<String> classIds);

    List<LinkedHashMap<String, Object>> getDataClassByBizUserOrDatabase(@Param("bizUserId")String bizUserId,@Param("database_id")String database_id);

    List<Map<String,Object>> getDataInfoByUserId(@Param("userId") String userId,@Param("dataClassId") String dataClassId);

    List<Map<String,Object>> getSpecialDBData(@Param("sdbId") String sdbId);

    List<DataClassEntity> getDataClassTree(@Param("databaseId")String databaseId);

    List<DataClassEntity> getDataClassTreePostgresql(@Param("databaseId")String databaseId);


    List<Map<String,Object>> getDatabaseName();

    List<Map<String,Object>>  getByDatabaseDefineId(@Param("id")String id);

    List<GridAreaEntity> getAreaByPage(@Param("map") Map<String, String> map);
}
