package com.piesat.sod.system.web.controller;

import com.piesat.sod.system.rpc.api.ServiceCodeService;
import com.piesat.sod.system.rpc.dto.ServiceCodeDto;
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
 * @author yaya
 * @description TODO
 * @date 2020/2/13 15:22
 */
@RestController
@RequestMapping("/system/serviceCodeManagement")
@Api(value="服务代码管理",tags= {"服务代码管理接口"})
public class ServiceCodeManagementController {

    @Autowired
    private ServiceCodeService serviceCodeService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<ServiceCodeDto>> list(ServiceCodeDto serviceCodeDto, int pageNum, int pageSize) {
        ResultT<PageBean<ServiceCodeDto>> resultT = new ResultT<>();
        PageForm<ServiceCodeDto> pageForm = new PageForm<>(pageNum, pageSize, serviceCodeDto);
        PageBean pageBean = serviceCodeService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    /**
     * 添加
     * @param serviceCodeDto
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody ServiceCodeDto serviceCodeDto) {
        try {
            ServiceCodeDto save = this.serviceCodeService.saveDto(serviceCodeDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<ServiceCodeDto> edit(@RequestBody ServiceCodeDto serviceCodeDto)
    {
        ResultT<ServiceCodeDto> resultT=new ResultT<>();
        serviceCodeDto= this.serviceCodeService.updateDto(serviceCodeDto);
        resultT.setData(serviceCodeDto);
        return resultT;
    }

    @GetMapping(value = "/getById")
    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    public ResultT<ServiceCodeDto> getById(String id)
    {
        ResultT<ServiceCodeDto> resultT=new ResultT<>();
        ServiceCodeDto serviceCodeDto= this.serviceCodeService.getDotById(id);
        resultT.setData(serviceCodeDto);
        return resultT;
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteByIds/{ids}")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    public ResultT<String> remove(@PathVariable String[] ids)
    {
        ResultT<String> resultT=new ResultT<>();
        List<String> list=new ArrayList();
        if(ids.length>0){
            list= Arrays.asList(ids);
            this.serviceCodeService.deleteRecordByIds(list);
        }
        return resultT;
    }

    @DeleteMapping("/deleteById")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    public ResultT<String> remove(String id)
    {
        ResultT<String> resultT=new ResultT<>();
        this.serviceCodeService.deleteById(id);
        return resultT;
    }

    @GetMapping(value = "/queryAll")
    @ApiOperation(value = "查询所有", notes = "查询所有")
    public ResultT queryAll()
    {
        ResultT resultT=new ResultT<>();
        List<ServiceCodeDto> serviceCodeDto= this.serviceCodeService.queryAll();
        resultT.setData(serviceCodeDto);
        return resultT;
    }
}
