package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.ConsistencyCheckService;
import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
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
 * @author yaya
 * @description TODO
 * @date 2020/2/14 10:14
 */
@RestController
@RequestMapping("/dm/consistencyCheck")
@Api(value = "一致性检查controller", tags = {"一致性检查"})
public class ConsistencyCheckController {

    @Autowired
    private ConsistencyCheckService consistencyCheckService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(ConsistencyCheckDto consistencyCheckDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<ConsistencyCheckDto> pageForm = new PageForm<>(pageNum, pageSize, consistencyCheckDto);
        PageBean pageBean = consistencyCheckService.selectPageList(pageForm);
        resultT.setData(pageBean);

        return resultT;
    }
}
