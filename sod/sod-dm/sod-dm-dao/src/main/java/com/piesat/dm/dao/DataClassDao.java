package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DataClassEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DataClassDao extends BaseDao<DataClassEntity> {

    void deleteByDataClassId(String dataClassId);

    DataClassEntity findByDataClassId(String dataClassId);

}
