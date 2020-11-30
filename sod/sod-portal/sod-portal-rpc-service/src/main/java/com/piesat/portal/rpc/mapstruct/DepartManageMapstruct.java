package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.DepartManageEntity;
import com.piesat.portal.rpc.dto.DepartManageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("departManageMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartManageMapstruct extends BaseMapper<DepartManageDto, DepartManageEntity> {
}
