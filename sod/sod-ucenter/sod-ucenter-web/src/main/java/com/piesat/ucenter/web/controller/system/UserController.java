package com.piesat.ucenter.web.controller.system;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 17:13
 */
@RestController
@Api(value="用户controller",tags={"用户操作接口"})
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping(value = "/api/user/save")
    @ApiOperation(value="添加用户接口", notes="添加用户接口")
    public UserDto save (@RequestBody  UserDto userDto){
       return userService.saveUserDto(userDto);
    }

}
