package com.piesat.schedule.rpc.mapstruct.backup;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.dto.backup.MetaBackupDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

import javax.persistence.Column;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-26 16:00
 **/
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaBackupMapstruct extends BaseMapper<MetaBackupDto,MetaBackupEntity>{

}

