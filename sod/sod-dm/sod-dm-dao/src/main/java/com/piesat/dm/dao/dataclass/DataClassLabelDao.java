package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassLabelEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据标签
 *
 * @author cwh
 * @date 2020年 07月31日 16:09:09
 */
@Repository
public interface DataClassLabelDao extends BaseDao<DataClassLabelEntity> {
    /**
     * 根据存储编码删除
     * @param dataclassId
     */
    void deleteByDataClassId(String dataclassId);

    /**
     * 根据存储编码查询
     * @param dataclassId
     * @return
     */
    List<DataClassLabelEntity> findByDataClassId(String dataclassId);

}
