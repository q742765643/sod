package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataOnlineTimeDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/17 19:19
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataOnlineTimeService {

    DataOnlineTimeDto saveDto(DataOnlineTimeDto dataOnlineTimeDto);

    void deleteByDataClassId(String dataClassId);

    PageBean onLineList(PageForm<Map<String,String>> pageForm);

    void update(DataOnlineTimeDto dataOnlineTimeDto);

    DataOnlineTimeDto findByDataClassId(String dataClassId);

}
