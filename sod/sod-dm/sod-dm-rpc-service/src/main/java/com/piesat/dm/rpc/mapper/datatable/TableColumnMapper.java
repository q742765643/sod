package com.piesat.dm.rpc.mapper.datatable;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月22日 15:52:19
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TableColumnMapper extends BaseMapper<TableColumnDto, TableColumnEntity> {
}
