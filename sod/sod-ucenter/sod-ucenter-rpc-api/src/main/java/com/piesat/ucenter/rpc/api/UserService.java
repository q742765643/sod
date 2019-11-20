package com.piesat.ucenter.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.ucenter.rpc.dto.UserDto;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 11:44
 */
@GrpcHthtService(server = "user",serialization = SerializeType.PROTOSTUFF)
public interface UserService {
     UserDto save(UserDto userDto);
}
