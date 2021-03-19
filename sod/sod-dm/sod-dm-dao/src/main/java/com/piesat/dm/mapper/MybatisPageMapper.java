package com.piesat.dm.mapper;

import com.piesat.dm.entity.datatable.GridAreaEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description: 分页查询
 * @date 2021年 03月18日 18:02:06
 */
@Component
public interface MybatisPageMapper {
    List<Map<String, Object>> selectStorageConfigurationPageList(@Param("map") Map<String, String> map);

    List<Map<String, Object>> storageFieldList(@Param("map") Map<String, String> map);

    List<Map<String, Object>> selectNewdataApplyPageList(@Param("map") Map<String, Object> map);

    List<Map<String, Object>> selectDataApplyPageList(@Param("map") Map<String, Object> map);

    /**
     * 在线时间检索条件分页查询
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> onLineList(@Param("map") Map<String, String> map);

    /**
     * 查询专题库资料列表
     * @param map
     * @return
     */
    List<Map<String, Object>> getDatabaseSpecialReadWriteList(@Param("map")Map<String,Object> map);


    List<GridAreaEntity> getAreaByPage(@Param("map") Map<String, String> map);

    /**
     * 分页条件查询表信息
     * @param map
     * @return
     */
    List<Map<String, Object>> getPageTableInfo(@Param("map") Map<String, Object> map);


    /**
     * 分页条件查询资料信息
     * @param map
     * @return
     */
    List<Map<String, Object>> getPageDataclassInfo(@Param("map") Map<String, Object> map);

    /**
     * 查询数据申请
     * @param map
     * @return
     */
    List<Map<String, Object>> getPageAuthorityDataApplyFlow(@Param("map") Map<String, String> map);

    List<Map<String, Object>> pageApplyData(@Param("map") Map<String, Object> map);
}
