package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.FeedbackManageService;
import com.piesat.portal.rpc.dto.FeedbackManageDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
