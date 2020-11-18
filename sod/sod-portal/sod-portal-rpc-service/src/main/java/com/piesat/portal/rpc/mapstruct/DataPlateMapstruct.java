package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.DataPlateEntity;
import com.piesat.portal.rpc.dto.DataPlateDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("dataPlateMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPlateMapstruct extends BaseMapper<DataPlateDto, DataPlateEntity> {
}
