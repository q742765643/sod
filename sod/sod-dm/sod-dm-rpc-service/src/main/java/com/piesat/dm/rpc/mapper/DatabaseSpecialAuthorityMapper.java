package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.DatabaseSpecialAuthorityEntity;
import com.piesat.dm.entity.DatabaseSpecialEntity;
import com.piesat.dm.rpc.dto.DatabaseSpecialAuthorityDto;
import com.piesat.dm.rpc.dto.DatabaseSpecialDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 数据库访问账户
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseSpecialAuthorityMapper extends BaseMapper<DatabaseSpecialAuthorityDto, DatabaseSpecialAuthorityEntity> {
}
