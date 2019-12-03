package com.piesat.ucenter.web.controller.system;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping(value = "/api/user/save")
    @ApiOperation(value="添加用户接口", notes="添加用户接口")
    public UserDto save (@RequestBody  UserDto userDto){
       return userService.saveUserDto(userDto);
    }
    @GetMapping("/list")
    public ResultT<PageBean> list(UserDto user,int pageNum,int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<UserDto> pageForm=new PageForm<>();
        pageForm.setCurrentPage(pageNum);
        pageForm.setPageSize(pageSize);
        pageForm.setT(user);
        PageBean pageBean=userService.selectUserList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }
}
