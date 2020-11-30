package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.DepartManageService;
import com.piesat.portal.rpc.dto.DepartManageDto;
import com.piesat.portal.rpc.util.PortalTreeSelect;
import com.piesat.ucenter.rpc.util.TreeSelect;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理
 */
@Api(value="部门管理",tags= {"部门管理"})
@RequestMapping("/portal/departManage")
@RestController
public class DepartManageController {

    @Autowired
    private DepartManageService departManageService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<DepartManageDto>> list(DepartManageDto departManageDto, int pageNum, int pageSize) {
        ResultT<PageBean<DepartManageDto>> resultT = new ResultT<>();
        PageForm<DepartManageDto> pageForm = new PageForm<>(pageNum, pageSize, departManageDto);
        PageBean pageBean = departManageService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }


    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            DepartManageDto departManageDto = this.departManageService.getDotById(id);
            return ResultT.success(departManageDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    /**
     * 获取部门下拉树列表
     */
    @ApiOperation(value = "获取部门下拉树列表")
    @GetMapping("/treeselect")
    public ResultT treeselect(DepartManageDto departManageDto)
    {
        ResultT resultT=new ResultT<>();
        List<PortalTreeSelect> portalTreeSelects = this.departManageService.getTreeSelectDept(departManageDto);
        resultT.setData(portalTreeSelects);
        return resultT;
    }

    @PostMapping(value="/save")
    @ApiOperation(value="添加",notes="添加")
    public ResultT save(@RequestBody DepartManageDto departManageDto){
        try {
            DepartManageDto save = this.departManageService.saveDto(departManageDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<DepartManageDto> edit(@RequestBody DepartManageDto departManageDto)
    {
        ResultT<DepartManageDto> resultT=new ResultT<>();
        departManageDto = this.departManageService.updateDto(departManageDto);
        resultT.setData(departManageDto);
        return resultT;
    }

    @ApiOperation(value = "根据id删除")
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.departManageService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
