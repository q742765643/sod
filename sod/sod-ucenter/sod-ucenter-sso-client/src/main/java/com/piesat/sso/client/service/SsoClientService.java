package com.piesat.sso.client.service;

import com.piesat.ucenter.rpc.api.system.MenuService;
import com.piesat.ucenter.rpc.api.system.RoleService;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.constant.GrpcConstant;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/13 10:12
 */
@Service
public class SsoClientService {
    @GrpcClient(GrpcConstant.UCENTER_SERVER)
    private RoleService roleService;
    @GrpcClient(GrpcConstant.UCENTER_SERVER)
    private MenuService menuService;
    @GrpcClient(GrpcConstant.UCENTER_SERVER)
    private UserService userService;
    public Set<String> getRolePermission(UserDto userDto){
        return roleService.getRolePermission(userDto);
    }
    public Set<String> getMenuPermission(UserDto userDto){
        return menuService.getMenuPermission(userDto);
    }

    public UserDto selectUserByUserName(String userName){
        return userService.selectUserByUserName(userName);
    }
    public UserDto selectUserByAppId(String appId){
        return userService.selectUserByAppId(appId);
    }
}
