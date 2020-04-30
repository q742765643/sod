package com.piesat.dm.dao.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataapply.NewdataTableColumnEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/18 9:52
 */
@Repository
public interface NewdataTableColumnDao extends BaseDao<NewdataTableColumnEntity> {
    List<NewdataTableColumnEntity> findByApplyId(String applyId);
    @Transactional
    void deleteByApplyId(String applyId);
}
