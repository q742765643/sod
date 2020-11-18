package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.TypicalAppService;
import com.piesat.portal.rpc.dto.TypicalAppDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 典型应用管理
 */
@Api(value="典型应用管理",tags= {"典型应用管理"})
@RequestMapping("/portal/typicalApp")
@RestController
public class TypicalAppController {

    @Autowired
    private TypicalAppService typicalAppService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<TypicalAppDto>> list(TypicalAppDto typicalAppDto, int pageNum, int pageSize) {
        ResultT<PageBean<TypicalAppDto>> resultT = new ResultT<>();
        PageForm<TypicalAppDto> pageForm = new PageForm<>(pageNum, pageSize, typicalAppDto);
        PageBean pageBean = typicalAppService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            TypicalAppDto typicalAppDto = this.typicalAppService.getDotById(id);
            return ResultT.success(typicalAppDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value="/save")
    @ApiOperation(value="添加",notes="添加")
    public ResultT save(@RequestBody TypicalAppDto dplPlateDto){
        try {
            TypicalAppDto save = this.typicalAppService.saveDto(dplPlateDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<TypicalAppDto> edit(@RequestBody TypicalAppDto typicalAppDto)
    {
        ResultT<TypicalAppDto> resultT=new ResultT<>();
        typicalAppDto = this.typicalAppService.updateDto(typicalAppDto);
        resultT.setData(typicalAppDto);
        return resultT;
    }

    @ApiOperation(value = "根据id删除")
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.typicalAppService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }





}
