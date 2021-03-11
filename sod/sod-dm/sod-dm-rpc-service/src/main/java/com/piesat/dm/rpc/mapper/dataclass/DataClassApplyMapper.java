package com.piesat.dm.rpc.mapper.dataclass;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataclass.DataClassApplyEntity;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import com.piesat.dm.rpc.dto.dataclass.DataClassApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
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
public interface DataClassApplyMapper extends BaseMapper<DataClassApplyDto, DataClassApplyEntity> {
}
