package com.piesat.dm.rpc.mapper.special;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.special.DatabaseSpecialTreeEntity;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialTreeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseSpecialTreeMapper extends BaseMapper<DatabaseSpecialTreeDto, DatabaseSpecialTreeEntity> {
}
