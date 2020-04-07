package com.piesat.dm.rpc.mapper.datatable;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.datatable.TableIndexEntity;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 表索引
 *
 * @author cwh
 * @date 2019年 11月22日 15:53:17
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TableIndexMapper extends BaseMapper<TableIndexDto, TableIndexEntity> {
}
