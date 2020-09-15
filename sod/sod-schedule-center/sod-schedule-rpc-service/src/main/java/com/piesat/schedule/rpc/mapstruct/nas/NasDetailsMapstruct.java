package com.piesat.schedule.rpc.mapstruct.nas;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.nas.NasDetailsEntity;
import com.piesat.schedule.rpc.dto.nas.NasDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @ClassName : NasDetailsMapstruct
 * @Description : nas详细权限转换工具类
 * @Author : zzj
 * @Date: 2020-09-07 11:25
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NasDetailsMapstruct extends BaseMapper<NasDetailsDto, NasDetailsEntity> {
}

