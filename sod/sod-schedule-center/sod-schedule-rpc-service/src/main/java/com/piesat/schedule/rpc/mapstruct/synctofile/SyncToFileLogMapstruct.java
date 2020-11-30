package com.piesat.schedule.rpc.mapstruct.synctofile;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.synctofile.SyncToFileEntity;
import com.piesat.schedule.entity.synctofile.SyncToFileLogEntity;
import com.piesat.schedule.rpc.dto.synctofile.SyncToFileDto;
import com.piesat.schedule.rpc.dto.synctofile.SyncToFileLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author cwh
 * @date 2020年 10月28日 15:29:45
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SyncToFileLogMapstruct extends BaseMapper<SyncToFileLogDto, SyncToFileLogEntity> {
}
