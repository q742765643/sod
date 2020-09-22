package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

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

}
