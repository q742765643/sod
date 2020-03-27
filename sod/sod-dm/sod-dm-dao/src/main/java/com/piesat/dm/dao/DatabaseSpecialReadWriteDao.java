package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DatabaseSpecialEntity;
import com.piesat.dm.entity.DatabaseSpecialReadWriteEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专题库管理
 */
@Repository
public interface DatabaseSpecialReadWriteDao extends BaseDao<DatabaseSpecialReadWriteEntity> {

    /**
     * 根据专题库ID及资料类别获取数据列表
     * @param sdbId
     * @param dataType
     * @return
     */
    List<DatabaseSpecialReadWriteEntity> findBySdbIdAndDataType(String sdbId, String dataType);

    void deleteBySdbId(String sdbId);
}
