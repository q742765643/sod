package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.ApiManageService;
import com.piesat.portal.rpc.dto.ApiManageDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 接口管理
 */
@Api(value="接口管理",tags= {"接口管理"})
@RequestMapping("/portal/apiManage")
@RestController
public class ApiManageController {

    @Autowired
    private ApiManageService apiManageService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<ApiManageDto>> list(ApiManageDto apiManageDto, int pageNum, int pageSize) {
        ResultT<PageBean<ApiManageDto>> resultT = new ResultT<>();
        PageForm<ApiManageDto> pageForm = new PageForm<>(pageNum, pageSize, apiManageDto);
        PageBean pageBean = apiManageService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @GetMapping("/findByApiSys")
    @ApiOperation(value = "根据系统查询", notes = "根据系统查询")
    public ResultT<List<ApiManageDto>> findByApiSys(ApiManageDto apiManageDto) {
        ResultT<List<ApiManageDto>> resultT = new ResultT<>();
        List<ApiManageDto> results = apiManageService.findByApiSys(apiManageDto);
        resultT.setData(results);
        return resultT;
    }

    @GetMapping("/findApiAll")
    @ApiOperation(value = "查询所有", notes = "查询所有")
    public ResultT<List<ApiManageDto>> findApiAll() {
        ResultT<List<ApiManageDto>> resultT = new ResultT<>();
        List<ApiManageDto> results = apiManageService.findApiAll();
        resultT.setData(results);
        return resultT;
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            Map<String,Object> result = this.apiManageService.getDotById(id);
            return ResultT.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @Log(title = "业务动态管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.apiManageService.deleteApi(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增接口信息")
    @PostMapping("/save")
    public ResultT<ApiManageDto> save(@RequestBody ApiManageDto apiManageDto) {
        try {
            ApiManageDto save = this.apiManageService.saveDto(apiManageDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<ApiManageDto> edit(@RequestBody ApiManageDto apiManageDto) {
        try {
            ApiManageDto updateDto = this.apiManageService.updateDto(apiManageDto);
            return ResultT.success(updateDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "接口批量导入")
    @PostMapping(value = "/upload")
    public ResultT uploadFile(MultipartHttpServletRequest request) {
        try {
            List<MultipartFile> fileList = request.getFiles("fileName");
            MultipartFile mf = fileList.get(0);
            ResultT resultT = this.apiManageService.uploadFile(mf, (String) request.getParameter("apiSys"));
            return resultT;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
