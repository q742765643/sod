package com.piesat.dm.rpc.mapper.dataclass;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataclass.DataClassUserEntity;
import com.piesat.dm.rpc.dto.dataclass.DataClassUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 数据标签
 *
 * @author cwh
 * @date 2020年 07月31日 17:04:55
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataClassUserMapper extends BaseMapper<DataClassUserDto, DataClassUserEntity> {
}
