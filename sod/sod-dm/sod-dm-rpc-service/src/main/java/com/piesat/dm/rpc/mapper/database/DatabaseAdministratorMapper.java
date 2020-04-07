package com.piesat.dm.rpc.mapper.database;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.database.DatabaseAdministratorEntity;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 数据库管理账户
 *
 * @author cwh
 * @date 2019年 11月22日 15:42:01
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseAdministratorMapper extends BaseMapper<DatabaseAdministratorDto, DatabaseAdministratorEntity> {
}
