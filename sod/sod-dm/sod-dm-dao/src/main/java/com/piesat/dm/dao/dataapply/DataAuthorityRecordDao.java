package com.piesat.dm.dao.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataapply.DataAuthorityRecordEntity;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/30 15:25
 */
@Repository
public interface DataAuthorityRecordDao extends BaseDao<DataAuthorityRecordEntity> {

    @Query(value="select distinct applyId from DataAuthorityRecordEntity")
    List<String> findAllApplyId();

    List<DataAuthorityRecordEntity> findByApplyId(String applyId);

    /**
     * 根据表id删除
     * @param tableId
     */
    void deleteByTableId(String tableId);

}
