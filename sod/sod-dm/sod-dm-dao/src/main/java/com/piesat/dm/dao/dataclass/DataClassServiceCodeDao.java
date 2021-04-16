package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassServiceCodeEntity;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;

/**
 * 服务编码
 * @author cuiwenhui
 */
@Repository
public interface DataClassServiceCodeDao extends BaseDao<DataClassServiceCodeEntity> {

    /**
     * 根据存储编码删除
     * @param dataclassId
     */
    void deleteByDataClassId(String dataclassId);
}
