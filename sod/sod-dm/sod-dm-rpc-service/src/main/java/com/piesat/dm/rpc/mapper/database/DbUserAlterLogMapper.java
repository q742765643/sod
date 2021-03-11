package com.piesat.dm.rpc.mapper.database;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.database.DbUserAlterLogEntity;
import com.piesat.dm.rpc.dto.database.DbUserAlterLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author cwh
 * @date 2020年 12月16日 15:10:59
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DbUserAlterLogMapper extends BaseMapper<DbUserAlterLogDto, DbUserAlterLogEntity> {
}
