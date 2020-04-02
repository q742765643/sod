package com.piesat.dm.rpc.mapper.dataclass;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataclass.DataClassBaseInfoEntity;
import com.piesat.dm.rpc.dto.dataclass.DataClassBaseInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 资料基础信息
 *
 * @author cwh
 * @date 2020年 04月02日 09:23:17
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataClassBaseInfoMapper extends BaseMapper<DataClassBaseInfoDto, DataClassBaseInfoEntity> {
}
