package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.LogicDefineEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogicDefineDao extends BaseDao<LogicDefineEntity> {

    List<LogicDefineEntity> findByLogicFlag(String logicFlag);
}
