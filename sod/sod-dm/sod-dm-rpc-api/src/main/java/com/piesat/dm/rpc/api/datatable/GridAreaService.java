package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.GridAreaDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:28:39
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface GridAreaService {
    GridAreaDto saveDto(GridAreaDto gridAreaDto);

    GridAreaDto getDotById(String id);

    void delete(String id);

    void deleteGridAreaByIds(String ids);

    List<GridAreaDto> all();

    PageBean list(PageForm<Map<String,String>> pageForm, String dataServiceId);

    List<GridAreaDto> findByDataServiceId(String dataServiceId);
}
