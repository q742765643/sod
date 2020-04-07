package com.piesat.dm.rpc.mapper.database;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.database.DatabaseUserEntity;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 数据库访问账户
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseUserMapper extends BaseMapper<DatabaseUserDto, DatabaseUserEntity> {
}
