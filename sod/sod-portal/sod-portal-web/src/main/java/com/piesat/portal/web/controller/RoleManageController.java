package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.RoleManageService;
import com.piesat.portal.rpc.dto.RoleManageDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色管理
 */
@Api(value="用户管理",tags= {"用户管理"})
@RequestMapping("/portal/roleManage")
@RestController
public class RoleManageController {
    @Autowired
    private RoleManageService roleManageService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(RoleManageDto role, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<RoleManageDto> pageForm=new PageForm<>(pageNum,pageSize,role);
        PageBean pageBean=roleManageService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @GetMapping("/findAllRole")
    @ApiOperation(value = "查询所有", notes = "查询所有")
    public ResultT<List<RoleManageDto>> findAllRole()
    {
        ResultT<List<RoleManageDto>> resultT=new ResultT<>();
        List<RoleManageDto> roleDtos=roleManageService.findAllRole();
        resultT.setData(roleDtos);
        return resultT;
    }

    /**
     * 新增角色
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加",notes = "添加")
    public ResultT save(@RequestBody RoleManageDto roleManageDto)
    {
        try {
            RoleManageDto save = this.roleManageService.saveDto(roleManageDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            RoleManageDto roleManageDto = this.roleManageService.getDotById(id);
            return ResultT.success(roleManageDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    /**
     * 修改保存角色
     */
    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<RoleManageDto> edit(@RequestBody RoleManageDto roleManageDto)
    {
        try {
            RoleManageDto save = this.roleManageService.updateDto(roleManageDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/{roleIds}")
    public ResultT<String> remove(@PathVariable String[] roleIds)
    {
        ResultT<String> resultT=new ResultT<>();
        List<String> list=new ArrayList();
        if(roleIds.length>0){
            list= Arrays.asList(roleIds);
            roleManageService.deleteRoleByIds(list);
        }
        return resultT;
    }

    /**
     * 状态修改
     */
    @ApiOperation(value = "修改角色启停状态", notes = "修改角色启停状态")
    @PutMapping("/changeStatus")
    public ResultT<RoleManageDto> changeStatus(@RequestBody RoleManageDto roleManageDto)
    {
        ResultT<RoleManageDto> resultT=new ResultT<>();
        RoleManageDto roleDto = roleManageService.updateRoleStatus(roleManageDto);
        resultT.setData(roleDto);
        return resultT;
    }


}
