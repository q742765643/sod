package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.DplPlateEntity;
import com.piesat.portal.rpc.dto.DplPlateDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("dplPlateMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DplPlateMapstruct extends BaseMapper<DplPlateDto, DplPlateEntity> {
}
