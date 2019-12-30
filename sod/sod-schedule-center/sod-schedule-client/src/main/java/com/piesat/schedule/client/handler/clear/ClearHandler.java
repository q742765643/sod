package com.piesat.schedule.client.handler.clear;

import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.entity.JobInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:22
 */
@Slf4j
@Service("clearHandler")
public class ClearHandler implements BaseHandler {
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        log.info("清除调用成功");
    }
}
