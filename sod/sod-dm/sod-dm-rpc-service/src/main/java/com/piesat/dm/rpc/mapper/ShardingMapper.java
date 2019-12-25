package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.ShardingEntity;
import com.piesat.dm.rpc.dto.ShardingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月16日 15:50:29
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShardingMapper extends BaseMapper<ShardingDto, ShardingEntity> {
}
