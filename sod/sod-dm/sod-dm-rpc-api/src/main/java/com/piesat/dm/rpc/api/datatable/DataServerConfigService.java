package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.DataServerConfigDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * 服务信息配置
 *
 * @author cwh
 * @date 2020年 02月12日 15:55:33
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataServerConfigService {
    DataServerConfigDto saveDto(DataServerConfigDto dataServerConfigDto);

    List<DataServerConfigDto> saveDtoList(List<DataServerConfigDto> dataServerConfigDtos);

    DataServerConfigDto getDotById(String id);

    void delete(String id);

    void delByIds(String ids);

    List<DataServerConfigDto> all();

    PageBean list(PageForm pageForm, String dataServiceId);

    int getDataServiceMaxNum(String data_service_id);;
}
