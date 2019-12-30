package com.piesat.schedule.client.handler.move;

import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.entity.JobInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:24
 */
@Slf4j
@Service("moveHandler")
public class MoveHandler implements BaseHandler {
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        log.info("迁移调用成功");
    }
}
