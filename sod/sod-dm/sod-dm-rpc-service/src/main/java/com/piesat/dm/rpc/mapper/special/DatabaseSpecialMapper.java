package com.piesat.dm.rpc.mapper.special;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.special.DatabaseSpecialEntity;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 数据库访问账户
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseSpecialMapper extends BaseMapper<DatabaseSpecialDto, DatabaseSpecialEntity> {
}
