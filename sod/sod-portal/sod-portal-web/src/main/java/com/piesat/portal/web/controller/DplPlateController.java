package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.DplPlateService;
import com.piesat.portal.rpc.dto.DplPlateDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据板块管理
 */
@Api(value="PORTAL算法板块管理",tags= {"PORTAL算法板块管理"})
@RequestMapping("/portal/dplPlate")
@RestController
public class DplPlateController {

    @Autowired
    private DplPlateService dplPlateService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<DplPlateDto>> list(DplPlateDto dplPlateDto, int pageNum, int pageSize) {
        ResultT<PageBean<DplPlateDto>> resultT = new ResultT<>();
        PageForm<DplPlateDto> pageForm = new PageForm<>(pageNum, pageSize, dplPlateDto);
        PageBean pageBean = dplPlateService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            DplPlateDto dataPlateDto = this.dplPlateService.getDotById(id);
            return ResultT.success(dataPlateDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value="/save")
    @ApiOperation(value="添加",notes="添加")
    public ResultT save(@RequestBody DplPlateDto dplPlateDto){
        try {
            DplPlateDto save = this.dplPlateService.saveDto(dplPlateDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<DplPlateDto> edit(@RequestBody DplPlateDto dplPlateDto)
    {
        ResultT<DplPlateDto> resultT=new ResultT<>();
        dplPlateDto = this.dplPlateService.updateDto(dplPlateDto);
        resultT.setData(dplPlateDto);
        return resultT;
    }

    @ApiOperation(value = "根据id删除")
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dplPlateService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }



}
