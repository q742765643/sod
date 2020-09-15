package com.piesat.schedule.rpc.mapstruct.nas;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.nas.NasQuotaEntity;
import com.piesat.schedule.rpc.dto.nas.NasQuotaDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @ClassName : NasQuotaMapstruct
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-14 17:39
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NasQuotaMapstruct extends BaseMapper<NasQuotaDto, NasQuotaEntity> {
}

