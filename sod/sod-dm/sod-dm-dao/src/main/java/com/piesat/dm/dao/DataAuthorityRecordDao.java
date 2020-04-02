package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DataAuthorityRecordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/30 15:25
 */
@Repository
public interface DataAuthorityRecordDao extends BaseDao<DataAuthorityRecordEntity> {
    List<DataAuthorityRecordEntity> findByApplyIdAndDataClassId(String applyId, String dataClassId);

    List<DataAuthorityRecordEntity> findByApplyIdAndDataClassIdAndDatabaseId(String applyId, String dataClassId,String databaseId);
}
