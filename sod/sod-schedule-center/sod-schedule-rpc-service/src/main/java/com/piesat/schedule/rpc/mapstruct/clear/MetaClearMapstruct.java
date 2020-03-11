package com.piesat.schedule.rpc.mapstruct.clear;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.clear.MetaClearEntity;
import com.piesat.schedule.rpc.dto.clear.MetaClearDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 10:52
 **/
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaClearMapstruct extends BaseMapper<MetaClearDto,MetaClearEntity>{
}

