package com.piesat.controller;


import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.constant.GrpcConstant;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GrpcHthtClient
    private UserService userService;
    /*@RequestMapping("/test2")
   public void test(){
        UserDto userDto=new UserDto();
        userDto.setCreateTime(new Date());
        userDto.setCertificates("111");
        userService.save(userDto);
        System.out.println( testService.test());

   }*/
    @RequiresPermissions("xxxx")
    @RequestMapping("/test2")
   public void test(){
      UserDto uu=userService.selectUserByUserName("admin");
      UserDto userDto= (UserDto) SecurityUtils.getSubject().getPrincipal();
      System.out.println(111);
   }
}
