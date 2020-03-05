package com.piesat.schedule.rpc.mapstruct.recover;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.rpc.dto.recover.MetaRecoverLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-03 10:31
 **/
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaRecoverLogMapstruct extends BaseMapper<MetaRecoverLogDto,MetaRecoverLogEntity>{
}

