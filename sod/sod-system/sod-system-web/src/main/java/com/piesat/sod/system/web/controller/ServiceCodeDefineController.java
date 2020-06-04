package com.piesat.sod.system.web.controller;

import com.piesat.sod.system.rpc.api.ServiceCodeDefineService;
import com.piesat.sod.system.rpc.dto.GribParameterDefineDto;
import com.piesat.sod.system.rpc.dto.ServiceCodeDefineDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 13:40
 */
@RestController
@RequestMapping("/system/serviceCodeDefine")
@Api(value="服务代码定义",tags= {"服务代码定义接口"})
public class ServiceCodeDefineController {
    @Autowired
    private ServiceCodeDefineService serviceCodeDefineService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(ServiceCodeDefineDto serviceCodeDefineDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<ServiceCodeDefineDto> pageForm = new PageForm<>(pageNum, pageSize, serviceCodeDefineDto);
        PageBean pageBean = serviceCodeDefineService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }


    @PostMapping(value = "/save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody ServiceCodeDefineDto serviceCodeDefineDto) {
        try {
            ServiceCodeDefineDto save = this.serviceCodeDefineService.saveDto(serviceCodeDefineDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<ServiceCodeDefineDto> edit(@RequestBody ServiceCodeDefineDto serviceCodeDefineDto)
    {
        ResultT<ServiceCodeDefineDto> resultT=new ResultT<>();
        serviceCodeDefineDto= this.serviceCodeDefineService.updateDto(serviceCodeDefineDto);
        resultT.setData(serviceCodeDefineDto);
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
            this.serviceCodeDefineService.deleteRecordByIds(list);
        }
        return resultT;
    }

    @ApiOperation(value = "根据服务代码查询")
    @RequiresPermissions("dm:serviceCodeDefine:findByDbFcstEle")
    @GetMapping(value = "/findByDbFcstEle")
    public ResultT findByDbFcstEle(String dbFcstEle){
        List<ServiceCodeDefineDto> byDbFcstEle = this.serviceCodeDefineService.findByDbFcstEle(dbFcstEle);
        byDbFcstEle = byDbFcstEle.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ServiceCodeDefineDto:: getUserFcstEle))), ArrayList::new));
        return ResultT.success(byDbFcstEle);
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:serviceCodeDefine:findById")
    @GetMapping(value = "/findById")
    public ResultT findById(String id){
        ServiceCodeDefineDto serviceCodeDefineDto = this.serviceCodeDefineService.findById(id);
        return ResultT.success(serviceCodeDefineDto);
    }

}
