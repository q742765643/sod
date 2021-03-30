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
     * 根据数据类型查询
     * @param dataType
     * @return
     */
    List<DataClassInfoEntity> findByDataTypeOrderByDataClassId(Integer dataType);
}
