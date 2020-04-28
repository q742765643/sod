package com.piesat.schedule.dao.move;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 16:22
 **/
@Repository
public interface MoveDao extends BaseDao<MoveEntity> {

    List<MoveEntity> findByDataClassId(String dataClassId);

    List<MoveEntity> findByDatabaseIdAndDataClassId(String databaseId,String dataClassId);

}

