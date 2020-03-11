package com.piesat.schedule.rpc.mapstruct.clear;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.clear.MetaClearEntity;
import com.piesat.schedule.entity.clear.MetaClearLogEntity;
import com.piesat.schedule.rpc.dto.clear.MetaClearLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 16:55
 **/
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaClearLogMapstruct extends BaseMapper<MetaClearLogDto,MetaClearLogEntity>{
}

