package com.piesat.schedule.rpc.mapstruct.backup;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.backup.MetaBackupLogEntity;
import com.piesat.schedule.rpc.dto.backup.MetaBackupLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 10:00
 **/
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaBackupLogMapstruct extends BaseMapper<MetaBackupLogDto,MetaBackupLogEntity>{
}

