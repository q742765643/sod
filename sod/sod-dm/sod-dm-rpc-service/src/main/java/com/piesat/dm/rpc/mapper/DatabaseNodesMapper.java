package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.DatabaseNodesEntity;
import com.piesat.dm.rpc.dto.DatabaseNodesDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 数据库节点信息
 *
 * @author cwh
 * @date 2019年 11月22日 15:46:10
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseNodesMapper extends BaseMapper<DatabaseNodesDto, DatabaseNodesEntity> {
}
