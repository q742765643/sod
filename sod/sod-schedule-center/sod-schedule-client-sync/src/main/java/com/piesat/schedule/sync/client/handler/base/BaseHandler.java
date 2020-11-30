package com.piesat.schedule.sync.client.handler.base;

import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.util.ResultT;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:14
 */

public interface BaseHandler {
    /**
     *
     * @param jobInfoEntity
     * @param resultT
     */
    void execute(JobInfoEntity jobInfoEntity, ResultT<String> resultT);
}
