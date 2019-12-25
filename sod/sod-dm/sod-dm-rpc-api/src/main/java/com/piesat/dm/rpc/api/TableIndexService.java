package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.TableIndexDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 表索引
 *
 * @author cwh
 * @date 2019年 11月22日 15:38:16
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface TableIndexService {
    TableIndexDto saveDto(TableIndexDto tableIndexDto);

    TableIndexDto getDotById(String id);

    void delete(String id);

    List<TableIndexDto> all();
}
