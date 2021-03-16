package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.RoleManageEntity;
import com.piesat.portal.rpc.dto.RoleManageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("roleManageMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleManageMapstruct extends BaseMapper<RoleManageDto, RoleManageEntity> {
}
