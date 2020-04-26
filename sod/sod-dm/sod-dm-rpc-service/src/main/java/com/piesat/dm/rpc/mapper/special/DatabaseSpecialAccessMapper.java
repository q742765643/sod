package com.piesat.dm.rpc.mapper.special;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.special.DatabaseSpecialAccessEntity;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialAccessDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/25 22:27
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseSpecialAccessMapper extends BaseMapper<DatabaseSpecialAccessDto, DatabaseSpecialAccessEntity> {
}
