package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DataClassEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataClassDao extends BaseDao<DataClassEntity> {

    void deleteByDataClassId(String dataClassId);

    DataClassEntity findByDataClassId(String dataClassId);
}
