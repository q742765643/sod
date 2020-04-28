package com.piesat.schedule.dao.clear;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.clear.ClearEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 09:40
 **/
@Repository
public interface ClearDao extends BaseDao<ClearEntity>{
    List<ClearEntity> findByDatabaseIdAndDataClassId(String databaseId,String dataClassId);
}

