package com.piesat.schedule.client.handler.base;

import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.util.ResultT;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:14
 */

public interface BaseHandler {
    public void execute(JobInfoEntity jobInfoEntity, ResultT<String> resultT);
}
