package com.piesat.schedule.mapper.sync;

import com.piesat.schedule.entity.sync.SyncConfigEntity;
import com.piesat.schedule.entity.sync.SyncFilterEntity;
import com.piesat.schedule.entity.sync.SyncTaskEntity;
import com.piesat.schedule.entity.sync.SyncTaskLogEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/13 16:57
 */
@Component
public interface SyncTaskMapper {
    public List<SyncTaskEntity> selectPageList(SyncTaskEntity syncTaskEntity);

    public List<SyncTaskLogEntity> selectLogPageList(SyncTaskLogEntity syncTaskLogEntity);

}
