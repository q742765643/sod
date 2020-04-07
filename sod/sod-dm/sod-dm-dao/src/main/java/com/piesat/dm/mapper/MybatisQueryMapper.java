package com.piesat.dm.mapper;

import com.piesat.dm.entity.dataapply.NewdataApplyEntity;
import com.piesat.dm.entity.datatable.CmccElementEntity;
import com.piesat.dm.entity.datatable.DatumTableEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 联表查询Mapper
 *
 * @author cwh
 * @date 2020年 02月12日 09:26:56
 */
@Component
public interface MybatisQueryMapper {
    List<Map<String, Object>> getDataClassListBYIn(@Param("classIds")List<String> classIds, @Param("className")String className, @Param("dDataId")String dDataId);
    List<Map<String, Object>> getDataLogicByDatabaseId(String databaseId);

    List<Map<String, Object>> selectStorageConfigurationPageList(@Param("map")Map<String,String> map);

    List<Map<String, Object>> storageFieldList(@Param("map")Map<String,String> map);

    void updateStorageConfigurationStatus(@Param("id")String id, @Param("column")String column, @Param("value")String value);

    void updateNewdataApplyStatus(@Param("entity") NewdataApplyEntity newdataApplyEntity);

    List<Map<String, Object>> getByUserIdAndApplyId(@Param("entity")NewdataApplyEntity newdataApplyEntity);

    List<Map<String, Object>> getColumnByIdAndDDataId(@Param("entity")NewdataApplyEntity newdataApplyEntity);

    List<Map<String, Object>> getByDatabaseIdAndTableName(@Param("databaseId")String databaseId, @Param("tableName")String tableName);


    List<Map<String, Object>> selectNewdataApplyPageList(@Param("map")Map<String,Object> map);

    List<Map<String, Object>> queryNewdataApplyByApplyId(@Param("id")String id);
    /**
     * 在线时间检索条件分页查询
     * @param map
     * @return
     */
    List<Map<String, Object>> onLineList(@Param("map")Map<String,String> map);

    List<Map<String, Object>> getLogicClassTree();
    List<Map<String, Object>> getDatabaseTree();
    List<Map<String, Object>> getDatabaseClassTree(@Param("id")String id);
    List<Map<String, Object>> getDatabaseClassTreeMysql(@Param("id")String id);
    List<Map<String, Object>> getDatabaseClassTreePMysql(@Param("classIds")List<String> classIds,@Param("id")String id);

    Map<String, Object> queryDataAuthorityApplyById(@Param("id")String id);

    List<Map<String, Object>> getRecordByApplyIdMysql(@Param("map")Map<String,String> map);
    List<Map<String, Object>> getRecordByApplyId(@Param("map")Map<String,String> map);

    void updateDataAuthorityRecord(@Param("id")String id,@Param("authorize")Integer authorize,@Param("cause")String cause);
    void updateDataAuthorityApply(@Param("id")String id,@Param("auditStatus")String auditStatus);

    int getDataServiceMaxNum(@Param("data_service_id")String data_service_id);

    List<Map<String, Object>> getOnlineTime(@Param("classDataId")String classDataId,@Param("statisticDate")String statisticDate);

    List<Map<String, Object>> getTableInfoByClassId(@Param("dataClassId") String dataClassId);

    List<Map<String, Object>> getInfoByUserIdMysql(@Param("userId") String userId);
    List<Map<String, Object>> getInfoByUserId(@Param("userId") String userId);

    List<Map<String, Object>> querySpecialReadWriteBySdbId(@Param("sdbId") String sdbId);

    List<Map<String, Object>> getLogicByUserId(@Param("databaseIds") String[] databaseIds);

    List<Map<String, Object>> getRecordListByUserIdMysql(@Param("userId") String userId);
    List<Map<String, Object>> getRecordListByUserId(@Param("userId") String userId);

    List<Map<String, Object>> getDataClassBaseInfo(@Param("cCoremetaId") String cCoremetaId);

    List<CmccElementEntity> queryCmccElements(DatumTableEntity datumTableEntity);
}
