package com.piesat.dm.rpc.mapper.database;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.database.DatabaseAuthorizedEntity;
import com.piesat.dm.rpc.dto.database.DatabaseAuthorizedDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2020年 12月21日 14:16:01
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseAuthorizedMapper extends BaseMapper<DatabaseAuthorizedDto, DatabaseAuthorizedEntity> {
}
