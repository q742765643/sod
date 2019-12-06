package com.piesat.ucenter.web.controller.system;

import com.piesat.common.constant.Constants;
import com.piesat.common.utils.IdUtils;
import com.piesat.common.utils.VerifyCodeUtils;
import com.piesat.common.utils.sign.Base64;
import com.piesat.ucenter.rpc.api.system.MenuService;
import com.piesat.ucenter.rpc.api.system.RoleService;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.ucenter.rpc.util.RouterVo;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            map.put("token", subject.getSession().getId());
            resultT.setData(map);
        } catch (LockedAccountException e) {
            resultT.setErrorMessage(ReturnCodeEnum.ReturnCodeEnum_402_ERROR);
        }catch (UnknownAccountException e){
            resultT.setErrorMessage(ReturnCodeEnum.ReturnCodeEnum_404_ERROR);
        }catch (IncorrectCredentialsException e){
            resultT.setErrorMessage(ReturnCodeEnum.ReturnCodeEnum_405_ERROR);
        }catch (AuthenticationException ex){
            resultT.setErrorMessage(ReturnCodeEnum.ReturnCodeEnum_405_ERROR);
        }
        return resultT;
    }
    @GetMapping("getInfo")
    public ResultT<Map<String,Object>> getInfo()
    {
        ResultT<Map<String,Object>> resultT=new ResultT<>();
        Map<String,Object> map=new HashMap<>();
        UserDto userDto=userService.selectUserByUserName("zzj");
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
        List<RouterVo> routerVos=menuService.getRouters("788d74efcb99498eba5fb89814070737");
        resultT.setData(routerVos);
        return resultT;
    }
    @GetMapping(value = "/unauth")
    public ResultT<String> unauth() {
        ResultT<String> resultT=new ResultT<>();
        resultT.setErrorMessage(ReturnCodeEnum.ReturnCodeEnum_401_ERROR);
        return resultT;
    }
    @GetMapping(value = "/unauthorized")
    public ResultT<String> unauthorized() {
        ResultT<String> resultT=new ResultT<>();
        resultT.setErrorMessage(ReturnCodeEnum.ReturnCodeEnum_403_ERROR);
        return resultT;
    }

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public ResultT<Map<String,Object>> getCode(HttpServletResponse response) throws IOException
    {
        ResultT<Map<String,Object>> resultT = new ResultT();

        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 唯一标识
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        //redisCache.setCacheObject(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 生成图片
        int w = 111, h = 36;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
        try
        {
            Map<String,Object> map=new HashMap<>();
            map.put("uuid", uuid);
            map.put("img", Base64.encode(stream.toByteArray()));
            resultT.setData(map);
            return resultT;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            resultT.setErrorMessage(e.getMessage());
            return resultT;
        }
        finally
        {
            stream.close();
        }
    }
    @PostMapping("/logout")
    public ResultT<String> logout(){
        ResultT<String> resultT=new ResultT<>();
        try {
            Subject currentUser = SecurityUtils.getSubject();//获取当前用户信息
            if(currentUser.isAuthenticated()){
                currentUser.logout();
                resultT.setMessage("退出成功");
            }
        } catch (Exception e) {
            resultT.setErrorMessage("退出失败");
        }
        return resultT;
    }
    public  static void main(String[] args ){
        String password = new Md5Hash("111111","zzj",2).toString();
        System.out.println(password);

    }
}
