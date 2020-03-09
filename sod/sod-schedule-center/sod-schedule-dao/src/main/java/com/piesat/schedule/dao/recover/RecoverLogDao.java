package com.piesat.schedule.dao.recover;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import org.springframework.stereotype.Repository;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-05 11:29
 **/
@Repository
public interface RecoverLogDao extends BaseDao<RecoverLogEntity> {
}

