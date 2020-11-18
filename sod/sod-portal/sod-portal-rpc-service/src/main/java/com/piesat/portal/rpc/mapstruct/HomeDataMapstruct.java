package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.HomeDataEntity;
import com.piesat.portal.rpc.dto.HomeDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("homeDataMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeDataMapstruct extends BaseMapper<HomeDataDto, HomeDataEntity> {
}
