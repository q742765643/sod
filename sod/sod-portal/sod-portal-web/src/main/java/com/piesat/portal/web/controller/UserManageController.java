package com.piesat.portal.web.controller;

import com.piesat.common.utils.StringUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 */
@Api(value="用户管理",tags= {"用户管理"})
@RequestMapping("/portal/userManage")
@RestController
public class UserManageController {
    @Autowired
    private UserManageService userManageService;

    @Value("${sysLevel.value:P}")
    private String sysLevel;

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


    @PutMapping("/editUserRole")
    @ApiOperation(value = "修改用户所属角色", notes = "修改用户所属角色")
    public ResultT<UserManageDto> editUserRole(@RequestBody UserManageDto userManageDto)
    {
        ResultT<UserManageDto> resultT=new ResultT<>();
        userManageDto = this.userManageService.editUserRole(userManageDto);
        resultT.setData(userManageDto);
        return resultT;
    }

    @PutMapping("/resetPwd")
    @ApiOperation(value = "重置密码", notes = "重置密码")
    public ResultT<UserManageDto> resetPwd(@RequestBody UserManageDto userManageDto)
    {
        ResultT<UserManageDto> resultT=new ResultT<>();
        userManageDto = this.userManageService.resetPwd(userManageDto);
        resultT.setData(userManageDto);
        return resultT;
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "添加",notes = "添加")
    public ResultT save(@RequestBody UserManageDto userManageDto) {
        try {
            UserManageDto save = this.userManageService.saveDto(userManageDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取部署级别")
    @GetMapping(value = "/getSysLevel")
    public ResultT<String> getSysLevel() {
        ResultT<String> resultT=new ResultT<>();
        try {
            if(StringUtils.isEmpty(this.sysLevel)){
                this.sysLevel = "P";
            }
            String sysLevel = this.sysLevel;
            resultT.setData(sysLevel);
            return resultT;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询同级部门以及下级部门用户")
    @GetMapping(value = "/getSubUsers")
    public ResultT getSubUsers(String id) {
        try {
            List<UserManageDto> userManageDtos = this.userManageService.getSubUsers(id);
            return ResultT.success(userManageDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询同级部门以及上级部门业务处用户")
    @GetMapping(value = "/getBusinessUser")
    public ResultT getBusinessUser(String id) {
        try {
            List<UserManageDto> userManageDtos = this.userManageService.getBusinessUser(id);
            return ResultT.success(userManageDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


}
