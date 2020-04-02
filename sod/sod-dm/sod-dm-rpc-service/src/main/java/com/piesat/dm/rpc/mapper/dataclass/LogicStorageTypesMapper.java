package com.piesat.dm.rpc.mapper.dataclass;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataclass.LogicStorageTypesEntity;
import com.piesat.dm.rpc.dto.dataclass.LogicStorageTypesDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 数据用途与存储类型对应关系
 *
 * @author cwh
 * @date 2019年 11月22日 15:51:16
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogicStorageTypesMapper extends BaseMapper<LogicStorageTypesDto, LogicStorageTypesEntity> {
}
