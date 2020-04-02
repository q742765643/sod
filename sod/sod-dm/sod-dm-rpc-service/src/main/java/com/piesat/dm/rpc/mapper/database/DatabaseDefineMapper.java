package com.piesat.dm.rpc.mapper.database;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.database.DatabaseDefineEntity;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月22日 15:43:39
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseDefineMapper extends BaseMapper<DatabaseDefineDto, DatabaseDefineEntity> {
}
