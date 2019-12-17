package com.piesat.ucenter.web.controller.system;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 17:13
 */
@RestController
@RequestMapping("/system/user")
@Api(value="用户controller",tags={"用户操作接口"})
public class UserController {
    @Autowired
    private UserService userService;

    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    public ResultT<PageBean> list(UserDto user,int pageNum,int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<UserDto> pageForm=new PageForm<>(pageNum,pageSize,user);
        PageBean pageBean=userService.selectUserList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    /**
     * 根据用户编号获取详细信息
     */
    @RequiresPermissions("system:user:query")
    @GetMapping(value = "/{userId}")
    public ResultT<UserDto> getInfo(@PathVariable String userId)
    {
        ResultT<UserDto> resultT=new ResultT<>();
        UserDto userDto= userService.selectUserById(userId);
        resultT.setData(userDto);
        return resultT;
    }

    /**
     * 新增用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody UserDto user)
    {
        /*if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));*/
        ResultT<String> resultT=new ResultT<>();
        userService.insertUser(user);
        return resultT;
    }
    /**
     * 修改用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody UserDto user)
    {
        /*userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());*/

        ResultT<String> resultT=new ResultT<>();
        userService.updateUser(user);
        return resultT;
    }
    /**
     * 删除用户
     */
    /**
     * 删除用户
     */
    @RequiresPermissions("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public  ResultT<String> remove(@PathVariable String[] userIds)
    {
        ResultT<String> resultT=new ResultT<>();
        List<String> list=new ArrayList();
        if(userIds.length>0){
            list= Arrays.asList(userIds);
            userService.deleteUserByIds(list);
        }
        return resultT;
    }
    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    public ResultT<String> changeStatus(@RequestBody UserDto user)
    {
       /* userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());*/
        ResultT<String> resultT=new ResultT<>();
        userService.updateUserStatus(user);
        return resultT;
    }


}
