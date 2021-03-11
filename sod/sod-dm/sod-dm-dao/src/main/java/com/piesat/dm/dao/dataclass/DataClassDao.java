package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface DataClassDao extends BaseDao<DataClassEntity> {
    @Transactional
    void deleteByDataClassId(String dataClassId);

    DataClassEntity findByDataClassId(String dataClassId);

    List<DataClassEntity> findByDDataId(String dDataId);

    List<DataClassEntity> findByDataClassIdAndCreateBy(String dataclassId,String userId);

    List<DataClassEntity> findByParentIdOrderByDataClassIdAsc(String parentId);

    List<DataClassEntity> findByParentIdOrderByDataClassIdDesc(String parentId);

    List<DataClassEntity> findByParentIdAndTypeAndDataClassIdLikeOrderByDataClassIdDesc(String parentId, int type, String DataClassId);

    List<DataClassEntity> findByTypeAndDataClassIdLikeOrderByDataClassIdDesc(int type, String dataclassId);

    List<DataClassEntity> findByDataClassIdInOrTypeOrderByDataClassIdAsc(List<String> classIds, Integer type);

    /**
     * 根据表id查询相关资料
     *
     * @param tableId
     * @return
     */
    @Query(value =
            "SELECT DISTINCT B.* FROM T_SOD_DATACLASS_TABLE A " +
                    "LEFT JOIN T_SOD_DATA_CLASS B ON  a.DATA_CLASS_ID = B.DATA_CLASS_ID " +
                    "WHERE A.TABLE_ID=?1 OR A.SUB_TABLE_ID=?1 ", nativeQuery = true)
    List<Map<String, Object>> getClassByTableId(String tableId);

    /**
     * 根据存储编码查询关联表
     *
     * @param dataclassId
     * @return
     */
    @Query(value =
            "SELECT E.STORAGE_TYPE,C.DATABASE_NAME,C.SCHEMA_NAME,C.DATABASE_CLASSIFY,C.STOP_USE,C.DATABASE_DEFINE_ID,D.DATABASE_NAME " +
            "DATABASE_NAME_F,D.DATABASE_INSTANCE,D.DATABASE_TYPE,F.DICT_LABEL,B.TABLE_ID,B.SUB_TABLE_ID,E.TABLE_NAME,G.TABLE_NAME SUB_TABLE_NAME " +
            "FROM T_SOD_DATACLASS_TABLE B " +
            "LEFT JOIN T_SOD_DATA_TABLE_INFO E ON B.TABLE_ID = E.ID " +
            "LEFT JOIN T_SOD_DATA_TABLE_INFO G ON B.SUB_TABLE_ID = G.ID " +
            "LEFT JOIN T_SOD_DATABASE C ON E.DATABASE_ID=C.ID " +
            "LEFT JOIN T_SOD_DATABASE_DEFINE D ON C.DATABASE_DEFINE_ID = D.ID " +
            "LEFT JOIN T_SOD_DICT_DATA F ON E.STORAGE_TYPE = F.DICT_VALUE AND F.DICT_TYPE = 'sys_storage_type'  WHERE B.DATA_CLASS_ID = ?1 ", nativeQuery = true)
    List<Map<String, Object>> getTableInfo(String dataclassId);



}
