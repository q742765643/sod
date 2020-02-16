package com.piesat.dm.mapper;

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
    void updateStorageConfigurationStatus(@Param("id")String id, @Param("column")String column, @Param("value")String value);

    List<Map<String, Object>> getByDatabaseIdAndTableName(@Param("databaseId")String databaseId, @Param("tableName")String tableName);
}
