package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.DataTableApplyEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataTableApplyDao extends BaseDao<DataTableApplyEntity> {

    /**
     * 根据申请id查询
     * @param applyId
     * @return
     */
    List<DataTableApplyEntity> findByApplyId(String applyId);
}
