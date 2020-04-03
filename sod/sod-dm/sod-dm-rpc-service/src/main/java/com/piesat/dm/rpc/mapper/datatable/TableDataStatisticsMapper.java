package com.piesat.dm.rpc.mapper.datatable;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.datatable.TableDataStatisticsEntity;
import com.piesat.dm.rpc.dto.datatable.TableDataStatisticsDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 表数据统计
 *
 * @author cwh
 * @date 2020年 02月13日 14:51:47
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TableDataStatisticsMapper extends BaseMapper<TableDataStatisticsDto, TableDataStatisticsEntity> {
}
