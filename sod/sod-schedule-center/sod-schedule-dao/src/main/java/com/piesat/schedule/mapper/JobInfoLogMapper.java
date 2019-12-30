package com.piesat.schedule.mapper;

import com.piesat.schedule.entity.JobInfoLogEntity;
import org.springframework.stereotype.Component;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 15:44
 **/
@Component
public interface JobInfoLogMapper {
    JobInfoLogEntity selectMaxTriggerTimeByJobId(String jobId);
}

