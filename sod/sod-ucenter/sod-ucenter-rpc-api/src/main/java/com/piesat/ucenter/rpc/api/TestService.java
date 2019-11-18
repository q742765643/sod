package com.piesat.ucenter.rpc.api;


import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/12 17:23
 */
@GrpcHthtService(server = "user",serialization = SerializeType.PROTOSTUFF)
public interface TestService {
     String test();
}
