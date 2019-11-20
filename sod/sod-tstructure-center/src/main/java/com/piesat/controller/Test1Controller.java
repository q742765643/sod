package com.piesat.controller;


import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.grpc.interceptor.LogClientGrpcInterceptor;
import com.piesat.ucenter.rpc.api.TestService;
import com.piesat.ucenter.rpc.api.UserService;
import com.piesat.ucenter.rpc.dto.UserDto;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/12 15:33
 */
@RestController
public class Test1Controller {
    @GrpcClient(value = "cloud-grpc-server",interceptors = {LogClientGrpcInterceptor.class})
    private TestService testService;
    @GrpcClient("cloud-grpc-server")
    private UserService userService;
    @RequestMapping("/test2")
   public void test(){
        UserDto userDto=new UserDto();
        userDto.setCreateTime(new Date());
        userDto.setCertificates("111");
        userService.save(userDto);
        System.out.println( testService.test());

   }
}
