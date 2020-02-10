package com.piesat.schedule.rpc.mapstruct.sync;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.sync.SyncTaskEntity;
import com.piesat.schedule.rpc.dto.sync.SyncTaskDto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/13 17:35
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SyncTaskMapstruct extends BaseMapper<SyncTaskDto, SyncTaskEntity> {
}
