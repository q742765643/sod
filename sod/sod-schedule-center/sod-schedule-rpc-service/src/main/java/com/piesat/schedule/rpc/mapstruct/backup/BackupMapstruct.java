package com.piesat.schedule.rpc.mapstruct.backup;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 16:57
 **/
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BackupMapstruct extends BaseMapper<BackUpDto,BackupEntity>{
}

