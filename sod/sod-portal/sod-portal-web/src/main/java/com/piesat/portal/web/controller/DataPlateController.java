package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.DataPlateService;
import com.piesat.portal.rpc.dto.DataPlateDto;
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
@Api(value="PORTAL数据板块管理",tags= {"PORTAL数据板块管理"})
@RequestMapping("/portal/dataPlate")
@RestController
public class DataPlateController {

    @Autowired
    private DataPlateService dataPlateService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<DataPlateDto>> list(DataPlateDto dataPlateDto, int pageNum, int pageSize) {
        ResultT<PageBean<DataPlateDto>> resultT = new ResultT<>();
        PageForm<DataPlateDto> pageForm = new PageForm<>(pageNum, pageSize, dataPlateDto);
        PageBean pageBean = dataPlateService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            DataPlateDto dataPlateDto = this.dataPlateService.getDotById(id);
            return ResultT.success(dataPlateDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value="/save")
    @ApiOperation(value="添加",notes="添加")
    public ResultT save(@RequestBody DataPlateDto dataPlateDto){
        try {
            DataPlateDto save = this.dataPlateService.saveDto(dataPlateDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataPlateService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<DataPlateDto> edit(@RequestBody DataPlateDto dataPlateDto)
    {
        ResultT<DataPlateDto> resultT=new ResultT<>();
        dataPlateDto = this.dataPlateService.updateDto(dataPlateDto);
        resultT.setData(dataPlateDto);
        return resultT;
    }
}
