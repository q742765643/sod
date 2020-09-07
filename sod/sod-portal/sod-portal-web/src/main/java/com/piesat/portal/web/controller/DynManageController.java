package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.DynManageService;
import com.piesat.portal.rpc.dto.DynManageDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
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
 * 业务动态管理
 */
@Api(tags = "业务动态管理")
@RequestMapping("/portal/dynManage")
@RestController
public class DynManageController {

    @Autowired
    private DynManageService dynManageService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(DynManageDto dynManageDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<DynManageDto> pageForm = new PageForm<>(pageNum, pageSize, dynManageDto);
        PageBean pageBean = dynManageService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @Log(title = "业务动态管理", businessType = BusinessType.INSERT)
    @PostMapping(value="/addDynManage")
    @ApiOperation(value="新增业务动态管理（portal）",notes="新增业务动态管理（portal）")
    public ResultT addDynManage(@RequestBody DynManageDto dynManageDto){
        try {
            DynManageDto save = this.dynManageService.saveDto(dynManageDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<DynManageDto> edit(@RequestBody DynManageDto dynManageDto)
    {
        ResultT<DynManageDto> resultT=new ResultT<>();
        dynManageDto= this.dynManageService.updateDto(dynManageDto);
        resultT.setData(dynManageDto);
        return resultT;
    }

    @DeleteMapping("/deleteByIds/{ids}")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    public ResultT<String> remove(@PathVariable String[] ids)
    {
        ResultT<String> resultT=new ResultT<>();
        List<String> list=new ArrayList();
        if(ids.length>0){
            list= Arrays.asList(ids);
            this.dynManageService.deleteRecordByIds(list);
        }
        return resultT;
    }

}
