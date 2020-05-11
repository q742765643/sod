package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DataClassDao extends BaseDao<DataClassEntity> {
    @Transactional
    void deleteByDataClassId(String dataClassId);

    DataClassEntity findByDataClassId(String dataClassId);

    List<DataClassEntity> findByDDataId(String dDataId);

    List<DataClassEntity> findByParentIdOrderByDataClassIdAsc(String parentId);

    List<DataClassEntity> findByParentIdOrderByDataClassIdDesc(String parentId);

    List<DataClassEntity> findByParentIdAndTypeOrderByDataClassIdDesc(String parentId,int type);

    List<DataClassEntity> findByDDataId();
}
