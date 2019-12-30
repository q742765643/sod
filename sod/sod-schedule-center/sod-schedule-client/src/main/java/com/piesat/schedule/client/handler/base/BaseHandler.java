package com.piesat.schedule.client.handler.base;

import com.piesat.schedule.entity.JobInfoEntity;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:14
 */
@Service
public interface BaseHandler {
    public void execute(JobInfoEntity jobInfoEntity);
}
