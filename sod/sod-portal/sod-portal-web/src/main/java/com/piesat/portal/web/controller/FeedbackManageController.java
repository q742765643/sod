package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.FeedbackManageService;
import com.piesat.portal.rpc.dto.FeedbackManageDto;
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
@Api(value="用户反馈管理",tags= {"用户反馈管理"})
@RequestMapping("/portal/feedbackManage")
@RestController
public class FeedbackManageController {

    @Autowired
    private FeedbackManageService feedbackManageService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<FeedbackManageDto>> list(FeedbackManageDto feedbackManageDto, int pageNum, int pageSize) {
        ResultT<PageBean<FeedbackManageDto>> resultT = new ResultT<>();
        PageForm<FeedbackManageDto> pageForm = new PageForm<>(pageNum, pageSize, feedbackManageDto);
        PageBean pageBean = feedbackManageService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<FeedbackManageDto> edit(@RequestBody FeedbackManageDto feedbackManageDto)
    {
        ResultT<FeedbackManageDto> resultT=new ResultT<>();
        feedbackManageDto = this.feedbackManageService.updateDto(feedbackManageDto);
        resultT.setData(feedbackManageDto);
        return resultT;
    }


    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            FeedbackManageDto feedbackManageDto = this.feedbackManageService.getDotById(id);
            return ResultT.success(feedbackManageDto);
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
            this.feedbackManageService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @DeleteMapping("/deleteByIds/{ids}")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    public ResultT<String> remove(@PathVariable String[] ids)
    {
        ResultT<String> resultT=new ResultT<>();
        List<String> list=new ArrayList();
        if(ids.length>0){
            list= Arrays.asList(ids);
            this.feedbackManageService.deleteRecordByIds(list);
        }
        return resultT;
    }

}
