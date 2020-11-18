package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.ApiCodeEntity;
import com.piesat.portal.rpc.dto.ApiCodeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("apiCodeMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiCodeMapstruct extends BaseMapper<ApiCodeDto, ApiCodeEntity> {
}
