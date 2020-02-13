package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.TableDataStatisticsDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * 表数据统计
 *
 * @author cwh
 * @date 2020年 02月13日 14:50:17
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface TableDataStatisticsService {
    TableDataStatisticsDto saveDto(TableDataStatisticsDto tableDataStatisticsDto);

    TableDataStatisticsDto getDotById(String id);

    void delete(String id);

    List<TableDataStatisticsDto> all();

    PageBean list(PageForm pageForm);
}
