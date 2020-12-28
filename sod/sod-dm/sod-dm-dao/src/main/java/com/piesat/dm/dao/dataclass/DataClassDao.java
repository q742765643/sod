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

}
