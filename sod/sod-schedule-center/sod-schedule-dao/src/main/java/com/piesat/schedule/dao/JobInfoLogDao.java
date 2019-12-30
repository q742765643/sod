package com.piesat.schedule.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.JobInfoLogEntity;
import org.springframework.stereotype.Repository;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 15:07
 **/
@Repository
public interface JobInfoLogDao extends BaseDao<JobInfoLogEntity> {
}

