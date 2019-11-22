package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月22日 15:37:38
 */
@GrpcHthtService(server = "database",serialization = SerializeType.PROTOSTUFF)
public interface TableColumnService {
}
