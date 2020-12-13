package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.MenuManageEntity;
import com.piesat.portal.rpc.dto.MenuManageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("menuManageMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuManageMapstruct extends BaseMapper<MenuManageDto, MenuManageEntity> {
}
