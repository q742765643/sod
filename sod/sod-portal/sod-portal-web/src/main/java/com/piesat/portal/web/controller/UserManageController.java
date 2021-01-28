package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.UserManageService;
import com.piesat.portal.rpc.dto.UserManageDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 */
@Api(value="用户管理",tags= {"用户管理"})
@RequestMapping("/portal/userManage")
@RestController
public class UserManageController {
    @Autowired
    private UserManageService userManageService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<UserManageDto>> list(UserManageDto userManageDto, int pageNum, int pageSize) {
        ResultT<PageBean<UserManageDto>> resultT = new ResultT<>();
        PageForm<UserManageDto> pageForm = new PageForm<>(pageNum, pageSize, userManageDto);
        PageBean pageBean = userManageService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "根据id删除")
    @Log(title = "业务动态管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.userManageService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            UserManageDto userManageDto = this.userManageService.getDotById(id);
            return ResultT.success(userManageDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/editById")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<UserManageDto> editById(@RequestBody UserManageDto userManageDto)
    {
        ResultT<UserManageDto> resultT=new ResultT<>();
        userManageDto = this.userManageService.updateDto(userManageDto);
        resultT.setData(userManageDto);
        return resultT;
    }

    @PutMapping("/resetPwd")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public ResultT<UserManageDto> resetPwd(@RequestBody UserManageDto userManageDto)
    {
        ResultT<UserManageDto> resultT=new ResultT<>();
        userManageDto = this.userManageService.resetPwd(userManageDto);
        resultT.setData(userManageDto);
        return resultT;
    }

    @PostMapping(value="/savePortalUser")
    @ApiOperation(value="新增用户（portal）",notes="新增用户（portal）")
    public ResultT savePortalUser(@RequestBody UserManageDto userManageDto){
        try {
            UserManageDto save = this.userManageService.saveDto(userManageDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
