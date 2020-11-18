package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.HomeDataService;
import com.piesat.portal.rpc.dto.HomeDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 首页数据管理
 */
@Api(value="PORTAL首页数据管理",tags= {"PORTAL首页数据管理"})
@RequestMapping("/portal/homeData")
@RestController
public class HomeDataController {

    @Autowired
    private HomeDataService homeDataService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<HomeDataDto>> list(HomeDataDto homeDataDto, int pageNum, int pageSize) {
        ResultT<PageBean<HomeDataDto>> resultT = new ResultT<>();
        PageForm<HomeDataDto> pageForm = new PageForm<>(pageNum, pageSize, homeDataDto);
        PageBean pageBean = homeDataService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            HomeDataDto homeDataDto = this.homeDataService.getDotById(id);
            return ResultT.success(homeDataDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value="/save")
    @ApiOperation(value="首页数据管理（portal）",notes="首页数据管理（portal）")
    public ResultT save(@RequestBody HomeDataDto homeDataDto){
        try {
            HomeDataDto save = this.homeDataService.saveDto(homeDataDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<HomeDataDto> edit(@RequestBody HomeDataDto homeDataDto)
    {
        ResultT<HomeDataDto> resultT=new ResultT<>();
        homeDataDto = this.homeDataService.updateDto(homeDataDto);
        resultT.setData(homeDataDto);
        return resultT;
    }

    @ApiOperation(value = "根据id删除")
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.homeDataService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


}