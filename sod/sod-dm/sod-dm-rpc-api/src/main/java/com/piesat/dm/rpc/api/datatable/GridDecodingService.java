package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.GridDecodingDto;
import com.piesat.dm.rpc.dto.datatable.GridDecodingList;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * 解码配置
 *
 * @author cwh
 * @date 2020年 02月12日 11:39:24
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface GridDecodingService {
    GridDecodingDto saveDto(GridDecodingDto gridDecodingDto);

    List<GridDecodingDto> saveList(GridDecodingList gridDecodingList);

    GridDecodingDto getDotById(String id);

    void delete(String id);

    void delByIds(String ids);

    List<GridDecodingDto> all();

    PageBean list(PageForm pageForm,String dataServiceId);
}
