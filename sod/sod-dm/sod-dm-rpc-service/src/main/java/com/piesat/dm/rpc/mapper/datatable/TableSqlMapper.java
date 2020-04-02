package com.piesat.dm.rpc.mapper.datatable;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.datatable.TableSqlEntity;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 表sql
 *
 * @author cwh
 * @date 2020年 04月02日 14:22:14
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TableSqlMapper extends BaseMapper<TableSqlDto, TableSqlEntity> {
}
