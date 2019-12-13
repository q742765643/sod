package com.piesat.ucenter.rpc.shiro;

import com.piesat.common.utils.ServletUtils;
import com.piesat.common.utils.ip.AddressUtils;
import com.piesat.common.utils.ip.IpUtils;
import com.piesat.ucenter.rpc.api.system.MenuService;
import com.piesat.ucenter.rpc.api.system.RoleService;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
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
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        UserDto userDto=null;
        String username = (String) authenticationToken.getPrincipal();
        if("0".equals(token.getLoginType())){
            userDto = userService.selectUserByUserName(username);
        }else{
            userDto =userService.selectUserByAppId(username);
        }
        if (userDto == null) {
            throw new UnknownAccountException();
        }
        if (userDto.getStatus().equals("1")) { //账户冻结
            throw new LockedAccountException();
        }
        if("1".equals(token.getLoginType())){
            userDto.setPassword(new Md5Hash("",username,2).toString());
        }
        ByteSource salt = ByteSource.Util.bytes(username);
        if(null!=token.getRequest()) {
            UserAgent userAgent = UserAgent.parseUserAgentString(token.getRequest().getHeader("User-Agent"));
            String ip = IpUtils.getIpAddr(token.getRequest());
            userDto.setLoginIp(ip);
            userDto.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
            userDto.setBrowser(userAgent.getBrowser().getName());
            userDto.setOs(userAgent.getOperatingSystem().getName());
        }
        userDto.setParams(token.getParam());
        userDto.setLoginDate(new Date());
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userDto, //用户名
                userDto.getPassword(), //密码
                salt,
                getName()  //realm name
        );
        return authenticationInfo;

    }
}
