package com.piesat.dm.rpc.mapper.datatable;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataclass.DataClassLabelDefEntity;
import com.piesat.dm.entity.datatable.DataTableApplyEntity;
import com.piesat.dm.rpc.dto.dataclass.DataClassLabelDefDto;
import com.piesat.dm.rpc.dto.datatable.DataTableApplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 表申请
 *
 * @author cwh
 * @date 2020年 07月31日 17:04:55
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataTableApplyMapper extends BaseMapper<DataTableApplyDto, DataTableApplyEntity> {
}
