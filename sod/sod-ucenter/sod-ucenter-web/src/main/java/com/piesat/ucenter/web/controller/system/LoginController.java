package com.piesat.ucenter.web.controller.system;

import com.piesat.ucenter.rpc.api.system.MenuService;
import com.piesat.ucenter.rpc.api.system.RoleService;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.ucenter.rpc.util.RouterVo;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/28 11:43
 */
@RestController
@Api(value="登录",tags={"用户登录"})
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    /**
     * 登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @param
     * @param uuid 唯一标识
     * @return 结果
     */
    @PostMapping("/login")
    public ResultT<Map<String,Object>> login(String username, String password, String code, String uuid)
    {
        ResultT<Map<String,Object>> resultT=new ResultT<>();
        Map<String,Object> map=new HashMap<>();
        // 生成令牌
        String token = "1111";
        map.put("token", token);
        resultT.setData(map);
        return resultT;
    }
    @GetMapping("getInfo")
    public ResultT<Map<String,Object>> getInfo()
    {
        ResultT<Map<String,Object>> resultT=new ResultT<>();
        Map<String,Object> map=new HashMap<>();
        UserDto userDto=userService.selectUserByUserName("admin");
        // 角色集合
        Set<String> roles = roleService.getRolePermission(userDto);
        Set<String> permissions = menuService.getMenuPermission(userDto);

        map.put("user", userDto);
        map.put("roles", roles);
        map.put("permissions", permissions);
        resultT.setData(map);
        return resultT;
    }
    @GetMapping("getRouters")
    public ResultT<List<RouterVo>>  getRouters()
    {
        ResultT<List<RouterVo>> resultT=new ResultT<>();
        List<RouterVo> routerVos=menuService.getRouters("1");
        resultT.setData(routerVos);
        return resultT;
    }
}
