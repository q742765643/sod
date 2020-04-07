package com.piesat.dm.rpc.mapper.datatable;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月22日 15:49:17
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataTableMapper extends BaseMapper<DataTableDto, DataTableEntity> {
}
