package com.piesat.schedule.client.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 18:14
 **/
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BackupToLogMapstruct extends BaseMapper<BackupEntity,BackupLogEntity> {
}

