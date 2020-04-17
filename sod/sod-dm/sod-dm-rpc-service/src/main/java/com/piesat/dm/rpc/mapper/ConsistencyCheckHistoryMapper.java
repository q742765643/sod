package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.ConsistencyCheckHistoryEntity;
import com.piesat.dm.rpc.dto.ConsistencyCheckHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/16 11:25
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConsistencyCheckHistoryMapper extends BaseMapper<ConsistencyCheckHistoryDto, ConsistencyCheckHistoryEntity> {
}
