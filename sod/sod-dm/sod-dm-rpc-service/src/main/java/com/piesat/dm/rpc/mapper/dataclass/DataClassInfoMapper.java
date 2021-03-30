package com.piesat.dm.rpc.mapper.dataclass;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataclass.DataClassInfoEntity;
import com.piesat.dm.rpc.dto.dataclass.DataClassInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 资料申请
 *
 * @author cwh
 * @date 2019年 11月22日 15:47:19
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataClassInfoMapper extends BaseMapper<DataClassInfoDto, DataClassInfoEntity> {
}
