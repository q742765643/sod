package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.TypicalAppEntity;
import com.piesat.portal.rpc.dto.TypicalAppDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("typicalAppMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TypicalAppMapstruct extends BaseMapper<TypicalAppDto, TypicalAppEntity> {
}
