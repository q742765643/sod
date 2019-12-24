package com.piesat.schedule.rpc.mapstruct.clear;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.rpc.dto.clear.ClearDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 11:14
 **/
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClearMapstruct extends BaseMapper<ClearDto,ClearEntity>{
}

