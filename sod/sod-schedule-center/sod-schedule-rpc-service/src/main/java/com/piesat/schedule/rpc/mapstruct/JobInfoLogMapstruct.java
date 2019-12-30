package com.piesat.schedule.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.JobInfoLogEntity;
import com.piesat.schedule.rpc.dto.JobInfoDto;
import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 16:49
 **/
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobInfoLogMapstruct extends BaseMapper<JobInfoLogDto,JobInfoLogEntity>{
}

