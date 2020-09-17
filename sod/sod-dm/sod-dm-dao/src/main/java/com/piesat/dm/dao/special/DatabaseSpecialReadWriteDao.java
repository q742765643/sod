package com.piesat.dm.dao.special;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.special.DatabaseSpecialReadWriteEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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
    @Transactional
    void deleteBySdbId(String sdbId);

    List<DatabaseSpecialReadWriteEntity> findBySdbIdAndDataClassId(String sdbId, String dataClassId);

    List<DatabaseSpecialReadWriteEntity> findBySdbId(String sdbId);

    void deleteByDataClassId(String dataClassId);
}
