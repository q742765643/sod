package com.piesat.ucenter.rpc.shiro;

import com.piesat.ucenter.rpc.api.system.MenuService;
import com.piesat.ucenter.rpc.api.system.RoleService;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/5 14:36
 */
public class HtShiroRealm extends AuthorizingRealm  {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserDto userDto= (UserDto) principalCollection.getPrimaryPrincipal();
        // 角色集合
        Set<String> roles = roleService.getRolePermission(userDto);
        authorizationInfo.addRoles(roles);
        Set<String> permissions = menuService.getMenuPermission(userDto);
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        UserDto userDto = userService.selectUserByUserName(username);
        if (userDto == null) {
            throw new UnknownAccountException();
        }
        if (userDto.getStatus().equals("1")) { //账户冻结
            throw new LockedAccountException();
        }
        ByteSource salt = ByteSource.Util.bytes(username);

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userDto, //用户名
                userDto.getPassword(), //密码
                salt,
                getName()  //realm name
        );
        return authenticationInfo;

    }
}
