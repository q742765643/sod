package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassBaseInfoEntity;
import org.springframework.stereotype.Repository;

/**
 * 资料基础信息
 *
 * @author cwh
 * @date 2020年 04月01日 15:44:20
 */
@Repository
public interface DataClassBaseInfoDao extends BaseDao<DataClassBaseInfoEntity> {
    DataClassBaseInfoEntity findByDataClassId(String dataClassId);
}
