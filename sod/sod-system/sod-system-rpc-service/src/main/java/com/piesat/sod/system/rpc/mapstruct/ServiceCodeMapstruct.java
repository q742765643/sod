package com.piesat.sod.system.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.ServiceCodeEntity;
import com.piesat.sod.system.rpc.dto.ServiceCodeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/13 15:35
 */
@Service
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceCodeMapstruct extends BaseMapper<ServiceCodeDto, ServiceCodeEntity> {
}
