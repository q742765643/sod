package com.piesat.sod.system.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.ServiceCodeDefineEntity;
import com.piesat.sod.system.rpc.dto.ServiceCodeDefineDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 14:10
 */
@Service
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceCodeDefineMapstruct extends BaseMapper<ServiceCodeDefineDto, ServiceCodeDefineEntity> {
}
