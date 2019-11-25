package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;

/**
 * 数据库定义
 * @author cwh
 * @date 2019年 11月22日 15:30:47
 */
@GrpcHthtService(server = "database",serialization = SerializeType.PROTOSTUFF)
public interface DatabaseDefineService {
}
