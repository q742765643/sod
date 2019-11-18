package com.piesat.ucenter.web.controller;

import com.piesat.common.utils.ResultT;
import com.piesat.ucenter.rpc.api.TestService;
import com.piesat.ucenter.rpc.api.UserService;
import com.piesat.ucenter.rpc.dto.User;
import com.piesat.ucenter.rpc.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 15:39
 */
@RestController
@Api(value="用户controller",tags={"用户操作接口"})
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping(value = "/api/user/save")
    @ApiOperation(value="添加用户接口", notes="添加用户接口")
    public ResultT<UserDto> test(@RequestBody UserDto userDto){
        ResultT<UserDto> resultT=new ResultT();
        userDto=userService.save(userDto);
        resultT.setResult(userDto);
        return resultT;
    }
}
