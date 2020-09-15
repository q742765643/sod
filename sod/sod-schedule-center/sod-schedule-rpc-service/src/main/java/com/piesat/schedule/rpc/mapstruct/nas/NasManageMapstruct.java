package com.piesat.schedule.rpc.mapstruct.nas;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.nas.NasManageEntity;
import com.piesat.schedule.rpc.dto.nas.NasManageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @ClassName : NasManageMapstruct
 * @Description : nas申请转换类
 * @Author : zzj
 * @Date: 2020-09-07 11:23
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NasManageMapstruct extends BaseMapper<NasManageDto, NasManageEntity> {
}

