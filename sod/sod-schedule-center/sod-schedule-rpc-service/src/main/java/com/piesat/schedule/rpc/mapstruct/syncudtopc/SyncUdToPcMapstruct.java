package com.piesat.schedule.rpc.mapstruct.syncudtopc;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcEntity;
import com.piesat.schedule.rpc.dto.syncudtopc.SyncUdToPcDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author cwh
 * @date 2020年 10月28日 15:29:45
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SyncUdToPcMapstruct extends BaseMapper<SyncUdToPcDto, SyncUdToPcEntity> {
}
