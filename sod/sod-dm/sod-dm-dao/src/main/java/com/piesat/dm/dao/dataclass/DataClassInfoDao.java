package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassInfoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cuiwenhui
 */
@Repository
public interface DataClassInfoDao extends BaseDao<DataClassInfoEntity> {
    /**
     * 根据数据类型查询（0：业务数据，1：业务中间数据）
     * @param dataType
     * @return
     */
    List<DataClassInfoEntity> findByDataTypeOrderByDataClassIdDesc(Integer dataType);

    /**
     * 根据存储编码查询
     * @param dataclassId
     * @return
     */
    List<DataClassInfoEntity> findByDataClassId(String dataclassId);
}
