package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.ReadAuthorityEntity;
import com.piesat.dm.rpc.dto.ReadAuthorityDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/27 11:00
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReadAuthorityMapper extends BaseMapper<ReadAuthorityDto, ReadAuthorityEntity> {
}
