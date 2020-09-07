package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.DynManageEntity;
import com.piesat.portal.rpc.dto.DynManageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("dynManageMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DynManageMapstruct extends BaseMapper<DynManageDto, DynManageEntity> {
}
