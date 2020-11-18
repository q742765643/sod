package com.piesat.portal.rpc.mapstruct;


import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.ApiParamEntity;
import com.piesat.portal.rpc.dto.ApiParamDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("apiParamMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiParamMapstruct extends BaseMapper<ApiParamDto, ApiParamEntity> {
}
