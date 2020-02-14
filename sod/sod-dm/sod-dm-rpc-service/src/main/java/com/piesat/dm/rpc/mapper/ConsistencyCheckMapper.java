package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.ConsistencyCheckEntity;
import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/14 11:20
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConsistencyCheckMapper extends BaseMapper<ConsistencyCheckDto, ConsistencyCheckEntity> {
}
