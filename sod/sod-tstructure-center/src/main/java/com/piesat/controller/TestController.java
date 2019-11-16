package com.piesat.controller;


import com.google.protobuf.ByteString;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.common.grpc.service.GrpcRequest;
import com.piesat.common.grpc.service.SerializeService;
import com.piesat.common.grpc.util.SerializeUtils;
import com.piesat.rpc.CommonServiceGrpc;
import com.piesat.rpc.GrpcGeneral;
import com.piesat.ucenter.rpc.api.UserService;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/12 15:33
 */
@RestController
public class TestController {
    @GrpcHthtClient
    private UserService userService;
    @RequestMapping("/test1")
   public void test(){
        userService.test();

   }
}
