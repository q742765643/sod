package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassLabelDefEntity;
import com.piesat.dm.entity.dataclass.DataClassLabelEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据标签定义
 *
 * @author cwh
 * @date 2020年 07月31日 16:09:09
 */
@Repository
public interface DataClassLabelDefDao extends BaseDao<DataClassLabelDefEntity> {
}
