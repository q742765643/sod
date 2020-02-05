package com.piesat.schedule.rpc.mapstruct.sync;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.sync.SyncTaskLogEntity;
import com.piesat.schedule.rpc.dto.sync.SyncTaskLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/15 14:53
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SyncTaskLogMapstruct extends BaseMapper<SyncTaskLogDto, SyncTaskLogEntity> {
}
