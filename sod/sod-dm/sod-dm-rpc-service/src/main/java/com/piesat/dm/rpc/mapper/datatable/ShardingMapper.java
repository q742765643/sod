package com.piesat.dm.rpc.mapper.datatable;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.datatable.TablePartEntity;
import com.piesat.dm.rpc.dto.datatable.TablePartDto;
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
public interface ShardingMapper extends BaseMapper<TablePartDto, TablePartEntity> {
}
